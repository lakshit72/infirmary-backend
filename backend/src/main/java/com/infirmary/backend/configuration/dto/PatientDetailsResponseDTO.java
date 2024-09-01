package com.infirmary.backend.configuration.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientDetailsResponseDTO {

    private PatientDTO patient;

    private MedicalDetailsDTO medicalDetails;

    public PatientDetailsResponseDTO(PatientDTO patient, MedicalDetailsDTO medicalDetails) {

        this.patient = patient;

        this.medicalDetails = medicalDetails;
    }
}
