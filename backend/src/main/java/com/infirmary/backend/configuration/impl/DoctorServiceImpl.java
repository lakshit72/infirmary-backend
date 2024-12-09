package com.infirmary.backend.configuration.impl;

import com.infirmary.backend.configuration.Exception.AppointmentNotFoundException;
import com.infirmary.backend.configuration.Exception.DoctorNotFoundException;
import com.infirmary.backend.configuration.dto.DoctorDTO;
import com.infirmary.backend.configuration.dto.MedicalDetailsDTO;
import com.infirmary.backend.configuration.dto.PatientDTO;
import com.infirmary.backend.configuration.dto.PatientDetails;
import com.infirmary.backend.configuration.dto.PrescriptionDTO;
import com.infirmary.backend.configuration.model.Appointment;
import com.infirmary.backend.configuration.model.CurrentAppointment;
import com.infirmary.backend.configuration.model.Doctor;
import com.infirmary.backend.configuration.model.Location;
import com.infirmary.backend.configuration.model.MedicalDetails;
import com.infirmary.backend.configuration.model.Patient;
import com.infirmary.backend.configuration.model.Prescription;
import com.infirmary.backend.configuration.repository.AppointmentRepository;
import com.infirmary.backend.configuration.repository.CurrentAppointmentRepository;
import com.infirmary.backend.configuration.repository.DoctorRepository;
import com.infirmary.backend.configuration.repository.LocationRepository;
import com.infirmary.backend.configuration.repository.MedicalDetailsRepository;
import com.infirmary.backend.configuration.repository.PrescriptionRepository;
import com.infirmary.backend.configuration.service.DoctorService;
import com.infirmary.backend.shared.utility.AppointmentQueueManager;
import com.infirmary.backend.shared.utility.FunctionUtil;
import com.infirmary.backend.shared.utility.MessageConfigUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import org.apache.logging.log4j.util.InternalException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.infirmary.backend.shared.utility.FunctionUtil.createSuccessResponse;

import java.time.LocalDate;

@Slf4j
@Transactional
@Service
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final MessageConfigUtil messageConfigUtil;
    private final CurrentAppointmentRepository currentAppointmentRepository;
    private final MedicalDetailsRepository medicalDetailsRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final LocationRepository locationRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository, AppointmentRepository appointmentRepository, MessageConfigUtil messageConfigUtil,CurrentAppointmentRepository currentAppointmentRepository, MedicalDetailsRepository medicalDetailsRepository, PrescriptionRepository prescriptionRepository, LocationRepository locationRepository) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.messageConfigUtil = messageConfigUtil;
        this.currentAppointmentRepository = currentAppointmentRepository;
        this.medicalDetailsRepository = medicalDetailsRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.locationRepository = locationRepository;
    }


    @Override
    public DoctorDTO getDoctorById(String id) throws DoctorNotFoundException {
        Optional<Doctor> doctor = doctorRepository.findByDoctorEmail(id);
        if (doctor.isEmpty()) {
            throw new DoctorNotFoundException(messageConfigUtil.getDoctorNotFoundException());
        }
        return new DoctorDTO(doctor.get());
    }


    @Override
    public Boolean getDoctorStatusById(String id) throws DoctorNotFoundException {
        if (Objects.isNull(id)) {
            throw new DoctorNotFoundException(messageConfigUtil.getDoctorNotFoundException());
        }
        Doctor doctor = doctorRepository.findByDoctorEmail(id).orElseThrow(
                () -> new IllegalArgumentException("Doctor Status Not Found for the Id: " + id)
        );

        return doctor.isStatus();
    }


    @Override
    public Doctor setDoctorStatus(String id, Boolean isDoctorCheckIn, Double latitude, Double longitude) throws DoctorNotFoundException {
        if (Objects.isNull(id)) {
            throw new DoctorNotFoundException(messageConfigUtil.getDoctorNotFoundException());
        }
        Doctor doctor = doctorRepository.findByDoctorEmail(id).orElseThrow(
                () -> new IllegalArgumentException("Doctor Status Not Found for the Id: " + id)
        );

        Optional<CurrentAppointment> curs = currentAppointmentRepository.findByDoctor(doctor);

        if(curs.isPresent()) throw new IllegalArgumentException("Appointment already Assigned for the doctor");


        if(isDoctorCheckIn){
            List<Location> locations = locationRepository.findAll();
            Location presentLocation = null;

            for(Location location:locations){
                if(FunctionUtil.IsWithinRadius(location.getLatitude(), location.getLongitude(), latitude, longitude)){
                    presentLocation = location;
                    break;
                }
            }

            if(presentLocation == null) throw new IllegalArgumentException("Must be present on location to Check In");
            doctor.setLocation(presentLocation);
        }else{
            doctor.setLocation(null);
        }

        doctor.setStatus(isDoctorCheckIn);
        return doctorRepository.save(doctor);
    }


    @Override
    public HashMap<String, Integer> getAppointmentCountByDate(LocalDate date) throws AppointmentNotFoundException {
        if (Objects.isNull(date)) {
            throw new IllegalArgumentException("Date not found");
        }
        HashMap<String, Integer> dayMetrics = new HashMap<>();
        Integer byDate = appointmentRepository.countByDate(date);
        int count = currentAppointmentRepository.countByAppointmentNotNull();
        int cnt = (byDate + currentAppointmentRepository.countByAppointmentNotNullAndAppointment_DateNot(date));

        dayMetrics.put("Total_Appointment", (cnt));
        dayMetrics.put("In_Queue", count);
        dayMetrics.put("Patients_left", (cnt - count));

        return dayMetrics;
    }


    @Override
    public HashMap<LocalDate, Prescription> getPrescriptionHistory(String email)
    {
        //put check
        List<Appointment> listOfAppointments = appointmentRepository.findByPatient_Email(email);
        HashMap<LocalDate, Prescription> mapOfPrescription = new HashMap<>();

        for (Appointment listOfAppointment : listOfAppointments) {
            mapOfPrescription.put(listOfAppointment.getDate(), listOfAppointment.getPrescription());
        }
        return mapOfPrescription;
    }


    @Override
    public List<DoctorDTO> getAvailableDoctors(Double latitude, Double longitude) throws DoctorNotFoundException {
        List<Location> locations = locationRepository.findAll();
        Location presentLocation = null;

        for(Location location:locations){
            if(FunctionUtil.IsWithinRadius(location.getLatitude(), location.getLongitude(), latitude, longitude)){
                presentLocation = location;
                break;
            }
        }

        if(presentLocation == null) throw new IllegalArgumentException("Must be present in the location to get Available doctors");


        List<Doctor> byStatus = doctorRepository.findByStatusTrueAndLocation(presentLocation);
        List<DoctorDTO> dtoList = byStatus.stream().map(DoctorDTO::new).toList();
        if (dtoList.isEmpty()) {
            throw new DoctorNotFoundException(messageConfigUtil.getDoctorNotFoundException());
        }
        return dtoList;
    }

    
    @Override
     public List<DoctorDTO> getAllDoctors() throws DoctorNotFoundException {
         List<Doctor> list = doctorRepository.findAll();
         if (list.isEmpty()) {
             throw new DoctorNotFoundException(messageConfigUtil.getDoctorNotFoundException());
         }
         return list.stream().map(DoctorDTO::new).toList();
     }
    
     
    @Override
    public PatientDetails getPatient(String doctorEmail) {
        CurrentAppointment currentAppointment = currentAppointmentRepository.findByDoctor_DoctorEmail(doctorEmail).orElseThrow(()-> new ResourceNotFoundException("No Patient Assigned"));

        if(currentAppointment.getAppointment() == null) throw new ResourceNotFoundException("No Appointment Found");

        Patient patient = currentAppointment.getAppointment().getPatient();

        PatientDetails resp = new PatientDetails();

        resp.setPatient(new PatientDTO(patient));
        resp.getPatient().setPassword("");
        MedicalDetails medicalDetails = medicalDetailsRepository.findByPatient_Email(patient.getEmail()).orElseThrow(()->
            new ResourceNotFoundException("No Medical Details Available")
        );

        resp.setMedicalDetails(new MedicalDetailsDTO(medicalDetails));

        List<PrescriptionDTO> presc = prescriptionRepository.findByPatient(patient).stream().map((pres)->(new PrescriptionDTO(pres))).toList();


        resp.setPrescriptions(presc);

        resp.setReason(currentAppointment.getAppointment().getAptForm().getReason());

        resp.setDocName(currentAppointment.getDoctor().getName());

        return resp; 

    }


    @Override
    public Integer getCurrentTokenNo(String docEmail) throws InternalException {
        CurrentAppointment currentAppointment = currentAppointmentRepository.findByDoctor_DoctorEmail(docEmail).orElseThrow(() -> new ResourceNotFoundException("No Appointment Assigned"));

        if(currentAppointment.getAppointment() == null){
            currentAppointment.setDoctor(null);
            currentAppointmentRepository.save(currentAppointment);
            throw new InternalException("Doctor assigned without a Appointment");
        }

        return currentAppointment.getAppointment().getTokenNo();

    }


    @Override
    public ResponseEntity<?> releasePatient(String email) {
        CurrentAppointment currentAppointment = currentAppointmentRepository.findByDoctor_DoctorEmail(email).orElseThrow(()->new ResourceNotFoundException("No Appointment Found"));

        if(currentAppointment.getDoctor() == null) throw new ResourceNotFoundException("No Such Doctor exists");

        Doctor doctor = currentAppointment.getDoctor();

        doctor.setStatus(true);
        doctor = doctorRepository.save(doctor);
        currentAppointment.setDoctor(null);

        if(currentAppointment.getAppointment() == null) throw new ResourceNotFoundException("No Appointment Found");

        Appointment appointment = currentAppointment.getAppointment();
        appointment.setDoctor(null);
        appointment.setWeight(null);
        appointment.setTemperature(0);
        appointmentRepository.save(appointment);

        currentAppointment = currentAppointmentRepository.save(currentAppointment);

        AppointmentQueueManager.addAppointmentToQueue(currentAppointment.getAppointment().getAppointmentId());

        return createSuccessResponse("Patient Released");
        
    }
}
