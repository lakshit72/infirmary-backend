package com.infirmary.backend.configuration.impl;

import com.infirmary.backend.configuration.Exception.MedicalDetailsNotFoundException;
import com.infirmary.backend.configuration.Exception.PatientNotFoundException;
import com.infirmary.backend.configuration.Exception.SapIdExistException;
import com.infirmary.backend.configuration.dto.AppointmentReqDTO;
import com.infirmary.backend.configuration.dto.MedicalDetailsDTO;
import com.infirmary.backend.configuration.dto.PatientDTO;
import com.infirmary.backend.configuration.dto.PatientDetailsResponseDTO;
import com.infirmary.backend.configuration.dto.PrescriptionDTO;
import com.infirmary.backend.configuration.dto.PrescriptionRes;
import com.infirmary.backend.configuration.model.Appointment;
import com.infirmary.backend.configuration.model.AppointmentForm;
import com.infirmary.backend.configuration.model.CurrentAppointment;
import com.infirmary.backend.configuration.model.Doctor;
import com.infirmary.backend.configuration.model.Location;
import com.infirmary.backend.configuration.model.MedicalDetails;
import com.infirmary.backend.configuration.model.Patient;
import com.infirmary.backend.configuration.repository.AppointmentFormRepository;
import com.infirmary.backend.configuration.repository.AppointmentRepository;
import com.infirmary.backend.configuration.repository.CurrentAppointmentRepository;
import com.infirmary.backend.configuration.repository.DoctorRepository;
import com.infirmary.backend.configuration.repository.LocationRepository;
import com.infirmary.backend.configuration.repository.MedicalDetailsRepository;
import com.infirmary.backend.configuration.repository.PatientRepository;
import com.infirmary.backend.configuration.service.PatientService;
import com.infirmary.backend.shared.utility.MessageConfigUtil;

import io.jsonwebtoken.security.SecurityException;

import com.infirmary.backend.shared.utility.AppointmentQueueManager;
import com.infirmary.backend.shared.utility.FunctionUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Transactional
@Service
public class PatientServiceImpl implements PatientService {
    private final MedicalDetailsRepository medicalDetailsRepository;
    private final PatientRepository patientRepository;
    private final MessageConfigUtil messageConfigUtil;
    private final CurrentAppointmentRepository currentAppointmentRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentFormRepository appointmentFormRepository;
    private final LocationRepository locationRepository;

    public PatientServiceImpl(PatientRepository patientRepository, MessageConfigUtil messageConfigUtil, MedicalDetailsRepository medicalDetailsRepository,CurrentAppointmentRepository currentAppointmentRepository,AppointmentRepository appointmentRepository,AppointmentFormRepository appointmentFormRepository,DoctorRepository doctorRepository,LocationRepository locationRepository) {
        this.patientRepository = patientRepository;
        this.messageConfigUtil = messageConfigUtil;
        this.medicalDetailsRepository = medicalDetailsRepository;
        this.currentAppointmentRepository = currentAppointmentRepository;
        this.appointmentRepository = appointmentRepository;
        this.appointmentFormRepository = appointmentFormRepository;
        this.doctorRepository = doctorRepository;
        this.locationRepository = locationRepository;
    }
    
    @Override
    public PatientDTO getPatientBySapEmail(String email) throws PatientNotFoundException {
        Optional<Patient> patient = patientRepository.findByEmail(email);
        if (patient.isEmpty()) {
            throw new PatientNotFoundException(messageConfigUtil.getPatientNotFound());
        }
        return new PatientDTO(patient.get());
    }

    @Override
    public void validatePatientData(PatientDTO patientDTO) {
        if (!FunctionUtil.isValidId(patientDTO.getEmail())) {
            throw new IllegalArgumentException("Sap Id not found!");
        }

        if (FunctionUtil.isNameInvalid(patientDTO.getName())) {
            throw new IllegalArgumentException("Invalid name entered");
        }

        patientRepository.findByEmail(patientDTO.getEmail()).ifPresent(patient -> {
            throw new SapIdExistException(messageConfigUtil.getSapIdExistException());
        });
    }

    @Override
    public MedicalDetailsDTO updatePatientDetails(String email,
                                                  MedicalDetailsDTO medicalDetailsDTO)
            throws MedicalDetailsNotFoundException {
        
        Optional<MedicalDetails> medCheck = medicalDetailsRepository.findByPatient_Email(
                email
        );
        MedicalDetails existingMedicalDetails;
        if (medCheck.isEmpty()) {
            existingMedicalDetails = new MedicalDetails(medicalDetailsDTO);
        }else{
            existingMedicalDetails = medCheck.get();
            existingMedicalDetails.updateFromMedicalDetailsDTO(medicalDetailsDTO);
        }
        existingMedicalDetails.setPatient(patientRepository.findByEmail(email).get());
        MedicalDetails updatedMedicalDetails = medicalDetailsRepository.save(existingMedicalDetails);

        return new MedicalDetailsDTO(updatedMedicalDetails);
    }

    @Override
    public PatientDetailsResponseDTO getAllDetails(String email) throws PatientNotFoundException, MedicalDetailsNotFoundException {

        Optional<Patient> patient = patientRepository.findByEmail(email);
        Patient currentPatient = patient.orElseThrow(() -> new PatientNotFoundException("Patient not found"));

        Optional<MedicalDetails> medicalDetails = medicalDetailsRepository.findByPatient_Email(email);
        if (medicalDetails.isEmpty()) {
            throw new MedicalDetailsNotFoundException("Medical Details not found for the user");
        }

        return new PatientDetailsResponseDTO(new PatientDTO(currentPatient), new MedicalDetailsDTO(medicalDetails.get()));
    }

    @Override
    public ResponseEntity<?> submitAppointment(String sapEmail, AppointmentReqDTO appointmentReqDTO, Double latitude, Double longitude) throws UsernameNotFoundException {
        
        Optional<CurrentAppointment> appointmentForm = currentAppointmentRepository.findByPatient_Email(sapEmail);
        CurrentAppointment currentAppointment = new CurrentAppointment();
        Optional<Patient> patient = patientRepository.findByEmail(sapEmail);
        Location presentLocation = null;
        
        if(patient.isEmpty()) throw new UsernameNotFoundException("User does not Exists");
        
        if(medicalDetailsRepository.findByPatient_Email(sapEmail).isEmpty()) throw new IllegalArgumentException("Medical details Not Set");

        List<Location> locations = locationRepository.findAll();

        for(Location location:locations){
            if(FunctionUtil.IsWithinRadius(latitude, longitude, location.getLatitude(), location.getLongitude())){
                presentLocation = location;
                break;
            }
        }

        if(presentLocation == null) throw new IllegalArgumentException("Submit Appointment after reaching the infirmary");

        if(appointmentForm.isEmpty()){
            currentAppointment.setPatient(patient.get());
            currentAppointment = currentAppointmentRepository.save(currentAppointment);
        }else{
            if(appointmentForm.get().getAppointment() != null) throw new IllegalArgumentException("Appointment already Queued");
            currentAppointment = appointmentForm.get();
        }
        
        AppointmentForm appointmentForm2 = new AppointmentForm(appointmentReqDTO);

        if(appointmentReqDTO.getPreferredDoctor() != null){
            Optional<Doctor> pref_doc = doctorRepository.findById(appointmentReqDTO.getPreferredDoctor());
            if(pref_doc.isEmpty()) throw new ResourceNotFoundException("No Such Doctor Exists");
            appointmentForm2.setPrefDoctor(pref_doc.get());
        }

        if(appointmentReqDTO.getIsFollowUp()){
            Optional<Appointment> prevAppointment = appointmentRepository.findFirstByPatient_EmailOrderByTimestampDesc(sapEmail);
            appointmentForm2.setPrevAppointment(prevAppointment.isEmpty()?null:prevAppointment.get());
        }

        appointmentForm2 = appointmentFormRepository.save(appointmentForm2);

        Appointment appointment = new Appointment();
        appointment.setPatient(patient.get());
        Long timeStamp = System.currentTimeMillis();

        LocalDate date = Instant.ofEpochMilli(timeStamp).atZone(ZoneId.systemDefault()).toLocalDate();

        appointment.setDate(date);
        appointment.setTimestamp(timeStamp);
        appointment.setAptForm(appointmentForm2);
        appointment.setTokenNo(appointmentRepository.countByDate(LocalDate.now())+1);
        appointment.setLocation(presentLocation);
        appointmentRepository.save(appointment);
        currentAppointment.setAppointment(appointment);

        currentAppointment = currentAppointmentRepository.save(currentAppointment);
        AppointmentQueueManager.addAppointmentToQueue(currentAppointment.getAppointment().getAppointmentId());
        return ResponseEntity.ok("Appointment Submitted");
    }

    public ResponseEntity<?> getStatus(String sapEmail) throws ResourceNotFoundException{
        Optional<CurrentAppointment> currApt = currentAppointmentRepository.findByPatient_Email(sapEmail);
        Map<String,String> resp = new HashMap<>();
        if(currApt.isEmpty()){
            resp.put("Appointment", null);
            resp.put("Doctor", null);
            resp.put("DoctorName", null);
            resp.put("TokenNo", null);
            return ResponseEntity.ok(resp);
        } else{
                resp.put("Appointment", currApt.get().getAppointment() == null?null:currApt.get().getAppointment().getAppointmentId().toString());
                resp.put("Doctor", currApt.get().getDoctor() == null ? null:currApt.get().getDoctor().getDoctorId().toString());
                resp.put("DoctorName",currApt.get().getDoctor() == null?null:currApt.get().getDoctor().getName());
                resp.put("TokenNo", currApt.get().getAppointment() == null ? null : currApt.get().getAppointment().getTokenNo() == null ? null :currApt.get().getAppointment().getTokenNo().toString());
                return ResponseEntity.ok(resp);
        }
    }

    public ResponseEntity<?> getToken(String sapEmail) throws ResourceNotFoundException{
        Optional<CurrentAppointment> appointment = currentAppointmentRepository.findByPatient_Email(sapEmail);
        if(appointment.isEmpty()) throw new ResourceNotFoundException("No Appointment Available");
        if(Objects.isNull(appointment.get().getAppointment())) throw new ResourceNotFoundException("No Current Appointment");
        return ResponseEntity.ok(appointment.get().getAppointment().getTokenNo());
    }

    @Override
    public ResponseEntity<?> getPrescriptions(String sapEmail,UUID aptId) {

        Appointment appointment = appointmentRepository.findById(aptId).orElseThrow(()-> new ResourceNotFoundException("No prescription Found"));

        if(!sapEmail.equals(appointment.getPatient().getEmail())) throw new SecurityException("Unauthorized");

       PrescriptionRes prescriptionRes = new PrescriptionRes();


        prescriptionRes.setPrescription(new PrescriptionDTO(appointment.getPrescription()));

        prescriptionRes.setDate(appointment.getDate());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        Date date = new Date(appointment.getTimestamp());

        prescriptionRes.setTime(simpleDateFormat.format(date));

        prescriptionRes.setResidenceType(medicalDetailsRepository.findByPatient_Email(sapEmail).orElseThrow(()->new ResourceNotFoundException("No Patient Found")).getResidenceType());

        return ResponseEntity.ok(prescriptionRes);
    }

    @Override
    public ResponseEntity<?> getAppointment(String sapEmail) {
        sapEmail = sapEmail.substring(0,sapEmail.indexOf("@")).concat(sapEmail.substring(sapEmail.indexOf("@")).replaceAll(",", "."));

        Patient patient = patientRepository.findByEmail(sapEmail).orElseThrow(()-> new ResourceNotFoundException("No Patient Found"));

        List<Appointment> aptList =  appointmentRepository.findAllByPatientAndPrescriptionNotNull(patient).stream().filter(apt -> !(AppointmentQueueManager.getAppointedQueue().contains(apt.getAppointmentId()))).toList();

        List<Map<String,String>> resp = new ArrayList<>();

        for(Appointment curApt : aptList){
            Map<String,String> apt = new HashMap<>();
            apt.put("appointmentId",curApt.getAppointmentId().toString());
            apt.put("date",curApt.getDate().toString());
            apt.put("token", curApt.getTokenNo().toString());
            resp.add(apt);
        }

        return ResponseEntity.ok(resp);
    }
}
