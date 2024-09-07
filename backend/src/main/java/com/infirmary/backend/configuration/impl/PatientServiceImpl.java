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

    public PatientServiceImpl(PatientRepository patientRepository, MessageConfigUtil messageConfigUtil, MedicalDetailsRepository medicalDetailsRepository) {
        this.patientRepository = patientRepository;
        this.messageConfigUtil = messageConfigUtil;
        this.medicalDetailsRepository = medicalDetailsRepository;
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

        if (!FunctionUtil.isValidId(email)) {
            throw new IllegalArgumentException("Invalid SAP EMAIL: " + email);
        }
        MedicalDetails existingMedicalDetails = medicalDetailsRepository.findByPatient_Email(
                email
        );
        if (Objects.isNull(existingMedicalDetails)) {
            throw new MedicalDetailsNotFoundException(
                    "Medical details not found for the SAP ID: " + email
            );
        }
        existingMedicalDetails.updateFromMedicalDetailsDTO(medicalDetailsDTO);

        MedicalDetails updatedMedicalDetails = medicalDetailsRepository.save(existingMedicalDetails);

        return new MedicalDetailsDTO(updatedMedicalDetails);
    }

    @Override
    public PatientDetailsResponseDTO getAllDetails(String email) throws PatientNotFoundException, MedicalDetailsNotFoundException {
        if (!FunctionUtil.isValidId(email)) {
            throw new IllegalArgumentException("Invalid SAP EMAIL: " + email);
        }
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
}
