package com.infirmary.backend.configuration.impl;

import com.infirmary.backend.configuration.Exception.InvalidDataException;
import com.infirmary.backend.configuration.Exception.PatientNotFoundException;
import com.infirmary.backend.configuration.Exception.SapIdExistException;
import com.infirmary.backend.configuration.dto.PatientDTO;
import com.infirmary.backend.configuration.model.Patient;
import com.infirmary.backend.configuration.repository.PatientRepository;
import com.infirmary.backend.configuration.service.PatientService;
import com.infirmary.backend.shared.utility.MessageConfigUtil;
import com.infirmary.backend.shared.utility.FunctionUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Transactional
@Service
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final MessageConfigUtil messageConfigUtil;
    private final FunctionUtil functionUtil;

    public PatientServiceImpl(PatientRepository patientRepository, MessageConfigUtil messageConfigUtil,
                              FunctionUtil functionUtil) {
        this.patientRepository = patientRepository;
        this.messageConfigUtil = messageConfigUtil;
        this.functionUtil = functionUtil;
    }
  
    @Override
    public PatientDTO getPatientBySapId(Long sapId) throws PatientNotFoundException{
        try {
            if (sapId == null || !FunctionUtil.isValidId(sapId)){
                throw new IllegalArgumentException("Wrong Sap Id");
            }
            Optional<Patient> patient = patientRepository.findBySapId(sapId);
            if (patient.isEmpty()){
                throw new PatientNotFoundException(messageConfigUtil.getPatientNotFound());
            }
            return new PatientDTO(patient.get());
        } catch (PatientNotFoundException e){
            log.error("PatientNotFound Exception in method getPatientBySapId", e);
            throw e;
        } catch (Exception e){
            log.error("Exception in getPatientBySapId", e);
            throw new RuntimeException(e);
        }
    }

    private boolean isIdValid(Long sapId){
        String sapIdstr = sapId.toString();
        return sapIdstr.matches("^5\\d{8}$");
    }
    public PatientDTO createPatient(PatientDTO patientDTO){
        try {
            Patient patient = new Patient(patientDTO);
            Patient savedPatient = patientRepository.save(patient);
            return new PatientDTO(savedPatient);
        } catch (Exception e){
            log.error("Exception in patient creation");
            throw new RuntimeException(e);
        }
    }

    public void validatePatientData(PatientDTO patientDTO) {
        try {
            if (!FunctionUtil.isValidId(patientDTO.getSapId())) {
                throw new IllegalArgumentException("Sap Id not found!");
            }

            validateRequiredFields(patientDTO);
            if (FunctionUtil.isNameInvalid(patientDTO.getName())){
                throw new IllegalArgumentException("Invalid name entered");
            }

            patientRepository.findBySapId(patientDTO.getSapId()).ifPresent(patient -> {
                throw new SapIdExistException(messageConfigUtil.getSapIdExistException());
            });

        } catch (SapIdExistException | InvalidDataException e) {
            log.error("{} in method validatePatientData: {}", e.getClass().getSimpleName(), e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Exception in method validatePatientData", e);
            throw new RuntimeException(e);
        }
    }

    private void validateRequiredFields(PatientDTO patientDTO) {
        if (Stream.of(patientDTO.getName(), patientDTO.getPhoneNumber(), patientDTO.getDateOfBirth(),
                        patientDTO.getEmergencyContact(), patientDTO.getAddress(),
                        patientDTO.getProgram(), patientDTO.getSchool())
                .anyMatch(String::isEmpty)) {
            throw new InvalidDataException(messageConfigUtil.getInvalidDataException());
        }
        if (patientDTO.getStudentDetails() == null) {
            throw new InvalidDataException("Student Details URL cannot be null");
        }
    }
}
