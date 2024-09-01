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
    @Override
    public PatientDetailsResponseDTO createPatient(PatientDTO patientDTO, MedicalDetailsDTO medicalDetailsDTO){
        try {
            Patient patient = new Patient(patientDTO);
            MedicalDetails medicalDetails = new MedicalDetails(medicalDetailsDTO);
            Patient savedPatient = patientRepository.save(patient);
            MedicalDetails savedMedicalDetails = medicalDetailsRepository.save(medicalDetails);
            return new PatientDetailsResponseDTO(
                    new PatientDTO(savedPatient),
                    new MedicalDetailsDTO(savedMedicalDetails));
        } catch (Exception e){
            log.error("Exception in patient creation");
            throw new RuntimeException(e);
        }
    }
    @Override
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
    @Override
    public PatientDetailsResponseDTO updatePatientDetails(Long sapId,
                                                          PatientDTO patientDTO,
                                                          MedicalDetailsDTO medicalDetailsDTO)
            throws PatientNotFoundException, MedicalDetailsNotFoundException {

        if (!FunctionUtil.isValidId(sapId)) {
            throw new IllegalArgumentException("Invalid SAP ID: " + sapId);
        }

        try {
            Optional<Patient> optionalPatient = patientRepository.findBySapId(sapId);
            Patient existingPatient = optionalPatient
                    .map(patient -> {
                        patient.updateFromDTO(patientDTO);
                        return patient;
                    })
                    .orElseThrow(() -> new PatientNotFoundException("Patient not found with SAP ID: " + sapId));

            MedicalDetails existingMedicalDetails = medicalDetailsRepository.findByPatient_SapId(
                    existingPatient.getSapId()
            );
            if (Objects.isNull(existingMedicalDetails)) {
                throw new MedicalDetailsNotFoundException(
                        "Medical details not found for the SAP ID: " + existingPatient.getSapId()
                );
            }
            existingMedicalDetails.updateFromMedicalDetailsDTO(medicalDetailsDTO);

            Patient updatedPatient = patientRepository.save(existingPatient);
            MedicalDetails updatedMedicalDetails = medicalDetailsRepository.save(existingMedicalDetails);

            return new PatientDetailsResponseDTO(
                    new PatientDTO(updatedPatient),
                    new MedicalDetailsDTO(updatedMedicalDetails));
        } catch (Exception e) {
            log.error("Exception in updatePatientDetails", e);
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
    }
}
