package com.infirmary.backend.configuration.impl;

import com.infirmary.backend.configuration.Exception.InvalidDataException;
import com.infirmary.backend.configuration.Exception.MedicalDetailsNotFoundException;
import com.infirmary.backend.configuration.Exception.PatientNotFoundException;
import com.infirmary.backend.configuration.Exception.SapIdExistException;
import com.infirmary.backend.configuration.dto.AppointmentReqDTO;
import com.infirmary.backend.configuration.dto.CurrentAppointmentDTO;
import com.infirmary.backend.configuration.dto.MedicalDetailsDTO;
import com.infirmary.backend.configuration.dto.PatientDTO;
import com.infirmary.backend.configuration.dto.PatientDetailsResponseDTO;
import com.infirmary.backend.configuration.model.Appointment;
import com.infirmary.backend.configuration.model.AppointmentForm;
import com.infirmary.backend.configuration.model.CurrentAppointment;
import com.infirmary.backend.configuration.model.MedicalDetails;
import com.infirmary.backend.configuration.model.Patient;
import com.infirmary.backend.configuration.repository.AppointmentFormRepository;
import com.infirmary.backend.configuration.repository.AppointmentRepository;
import com.infirmary.backend.configuration.repository.CurrentAppointmentRepository;
import com.infirmary.backend.configuration.repository.MedicalDetailsRepository;
import com.infirmary.backend.configuration.repository.PatientRepository;
import com.infirmary.backend.configuration.service.PatientService;
import com.infirmary.backend.shared.utility.MessageConfigUtil;
import com.infirmary.backend.shared.utility.AppointmentQueueManager;
import com.infirmary.backend.shared.utility.FunctionUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Transactional
@Service
public class PatientServiceImpl implements PatientService {
    private final MedicalDetailsRepository medicalDetailsRepository;
    private final PatientRepository patientRepository;
    private final MessageConfigUtil messageConfigUtil;
    private final CurrentAppointmentRepository currentAppointmentRepository;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentFormRepository appointmentFormRepository;

    public PatientServiceImpl(PatientRepository patientRepository, MessageConfigUtil messageConfigUtil, MedicalDetailsRepository medicalDetailsRepository,CurrentAppointmentRepository currentAppointmentRepository,AppointmentRepository appointmentRepository,AppointmentFormRepository appointmentFormRepository) {
        this.patientRepository = patientRepository;
        this.messageConfigUtil = messageConfigUtil;
        this.medicalDetailsRepository = medicalDetailsRepository;
        this.currentAppointmentRepository = currentAppointmentRepository;
        this.appointmentRepository = appointmentRepository;
        this.appointmentFormRepository = appointmentFormRepository;
    }

    @Override
    public PatientDTO getPatientBySapEmail(String email) throws PatientNotFoundException {
        if (email == null || !FunctionUtil.isValidId(email)) {
            throw new IllegalArgumentException("Wrong Sap Id");
        }
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

        validateRequiredFields(patientDTO);
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
        
        MedicalDetails existingMedicalDetails = medicalDetailsRepository.findByPatient_Email(
                email
        );
        if (Objects.isNull(existingMedicalDetails)) {
            existingMedicalDetails = new MedicalDetails(medicalDetailsDTO);
        }else{
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

        MedicalDetails medicalDetails = medicalDetailsRepository.findByPatient_Email(email);
        if (Objects.isNull(medicalDetails)) {
            throw new MedicalDetailsNotFoundException("Medical Details not found for the user");
        }

        return new PatientDetailsResponseDTO(new PatientDTO(currentPatient), new MedicalDetailsDTO(medicalDetails));
    }

    private void validateRequiredFields(PatientDTO patientDTO) {
        if (Stream.of(patientDTO.getName(), patientDTO.getPhoneNumber(), patientDTO.getDateOfBirth(),
                        patientDTO.getEmergencyContact(),
                        patientDTO.getProgram(), patientDTO.getSchool())
                .anyMatch(String::isEmpty)) {
            throw new InvalidDataException(messageConfigUtil.getInvalidDataException());
        }
    }

    @Override
    public ResponseEntity<?> submitAppointment(String sapEmail,AppointmentReqDTO appointmentReqDTO) throws UsernameNotFoundException {
        Optional<CurrentAppointment> appointmentForm = currentAppointmentRepository.findByPatient_Email(sapEmail);
        CurrentAppointment currentAppointment = new CurrentAppointment();
        Optional<Patient> patient = patientRepository.findByEmail(sapEmail);
        if(patient.isEmpty()) throw new UsernameNotFoundException("User does not Exists");
        if(appointmentForm.isEmpty()){
            currentAppointment.setPatient(patient.get());
            currentAppointment = currentAppointmentRepository.save(currentAppointment);
        }else{
            currentAppointment = appointmentForm.get();
        }

        AppointmentForm appointmentForm2 = new AppointmentForm(appointmentReqDTO);
        appointmentForm2 = appointmentFormRepository.save(appointmentForm2);

        Appointment appointment = new Appointment(null);
        appointment.setPatient(patient.get());
        appointment.setDate(LocalDate.now());
        appointment.setAptForm(appointmentForm2);
        appointmentRepository.save(appointment);
        currentAppointment.setAppointment(appointment);

        currentAppointment = currentAppointmentRepository.save(currentAppointment);
        AppointmentQueueManager.addAppointmentToQueue(currentAppointment.getCurrentAppointmentId());
        return ResponseEntity.ok("Some");
    }

    public ResponseEntity<?> getStatus(String sapEmail) throws ResourceNotFoundException{
        Optional<CurrentAppointment> currApt = currentAppointmentRepository.findByPatient_Email(sapEmail);
        Map<String,Object> resp = new HashMap<>();
        if(currApt.isEmpty()){
            resp.put("Appointment", null);
            resp.put("Doctor", null);
            return ResponseEntity.ok(resp);
        } else{
                resp.put("Appointment", currApt.get().getAppointment());
                resp.put("Doctor", currApt.get().getDoctor());
                return ResponseEntity.ok(resp);
        }
    }
}
