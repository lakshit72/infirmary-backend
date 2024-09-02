package com.infirmary.backend.configuration.impl;

import com.infirmary.backend.configuration.Exception.InvalidDataException;
import com.infirmary.backend.configuration.Exception.MedicalDetailsNotFoundException;
import com.infirmary.backend.configuration.Exception.PatientNotFoundException;
import com.infirmary.backend.configuration.Exception.SapIdExistException;
import com.infirmary.backend.configuration.dto.MedicalDetailsDTO;
import com.infirmary.backend.configuration.dto.PatientDTO;
import com.infirmary.backend.configuration.dto.PatientDetailsResponseDTO;
import com.infirmary.backend.configuration.model.MedicalDetails;
import com.infirmary.backend.configuration.model.Patient;
import com.infirmary.backend.configuration.repository.MedicalDetailsRepository;
import com.infirmary.backend.configuration.repository.PatientRepository;
import com.infirmary.backend.configuration.service.PatientService;
import com.infirmary.backend.shared.utility.MessageConfigUtil;
import com.infirmary.backend.shared.utility.FunctionUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    private final FunctionUtil functionUtil;
    private final MedicalDetails medicalDetails;

    public PatientServiceImpl(PatientRepository patientRepository, MessageConfigUtil messageConfigUtil,
                              FunctionUtil functionUtil, MedicalDetails medicalDetails,
                              MedicalDetailsRepository medicalDetailsRepository) {
        this.patientRepository = patientRepository;
        this.messageConfigUtil = messageConfigUtil;
        this.functionUtil = functionUtil;
        this.medicalDetails = medicalDetails;
        this.medicalDetailsRepository = medicalDetailsRepository;
    }
    @Override
    public PatientDTO getPatientBySapEmail(Long id) throws PatientNotFoundException{
        try {
            if (id == null || !FunctionUtil.isValidId(id)){
                throw new IllegalArgumentException("Wrong Sap Id");
            }
            Optional<Patient> patient = patientRepository.findBySapId(id);
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
    @Override
    public void validatePatientData(PatientDTO patientDTO) {
        try {
            if (!FunctionUtil.isValidId(patientDTO.getId())) {
                throw new IllegalArgumentException("Sap Id not found!");
            }

            validateRequiredFields(patientDTO);
            if (FunctionUtil.isNameInvalid(patientDTO.getName())){
                throw new IllegalArgumentException("Invalid name entered");
            }

            patientRepository.findBySapId(patientDTO.getId()).ifPresent(patient -> {
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
    @Override
    public MedicalDetailsDTO updatePatientDetails(Long id,
                                                          MedicalDetailsDTO medicalDetailsDTO)
            throws MedicalDetailsNotFoundException {

        if (!FunctionUtil.isValidId(id)) {
            throw new IllegalArgumentException("Invalid SAP EMAIL: " + id);
        }
        try {

            MedicalDetails existingMedicalDetails = medicalDetailsRepository.findByPatient_SapId(
                    id
            );
            if (Objects.isNull(existingMedicalDetails)) {
                throw new MedicalDetailsNotFoundException(
                        "Medical details not found for the SAP ID: " + id
                );
            }
            existingMedicalDetails.updateFromMedicalDetailsDTO(medicalDetailsDTO);

            MedicalDetails updatedMedicalDetails = medicalDetailsRepository.save(existingMedicalDetails);

            return new MedicalDetailsDTO(updatedMedicalDetails);
        } catch (Exception e) {
            log.error("Exception in updatePatientDetails", e);
            throw new RuntimeException(e);
        }
    }
    @Override
    public PatientDetailsResponseDTO getAllDetails(Long id) throws PatientNotFoundException, MedicalDetailsNotFoundException {
        if (!FunctionUtil.isValidId(id)) {
            throw new IllegalArgumentException("Invalid SAP EMAIL: " + id);
        }
        try {
            Optional<Patient> patient = patientRepository.findBySapId(id);
            Patient currentPatient = patient.orElseThrow(()-> new PatientNotFoundException("Patient not found"));

            MedicalDetails medicalDetails = medicalDetailsRepository.findByPatient_SapId(id);
            if (Objects.isNull(medicalDetails)){
                throw new MedicalDetailsNotFoundException("Medical Details not found for the user");
            }

            return new PatientDetailsResponseDTO(new PatientDTO(currentPatient), new MedicalDetailsDTO(medicalDetails));
        } catch (Exception e){
            log.error("Exception in getAllDetails", e);
            throw e;
        }
    }

    private void validateRequiredFields(PatientDTO patientDTO) {
        if (Stream.of(patientDTO.getName(), patientDTO.getPhoneNumber(), patientDTO.getDateOfBirth(),
                        patientDTO.getEmergencyContact(),
                        patientDTO.getProgram(), patientDTO.getSchool())
                .anyMatch(String::isEmpty)) {
            throw new InvalidDataException(messageConfigUtil.getInvalidDataException());
        }
    }

}
