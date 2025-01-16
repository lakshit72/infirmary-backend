package com.infirmary.backend.configuration.dto;


import java.util.UUID;

import com.infirmary.backend.configuration.model.MedicalDetails;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MedicalDetailsDTO {
    private UUID id;

    private String image;
    
    @NotBlank(message = "Please provide your current address")
    private String currentAddress;
    
    @NotBlank(message = "Please Provide your medical history, if none write none")
    private String medicalHistory;
    
    @NotBlank(message = "Please Provide your family medical history, if none write none")
    private String familyMedicalHistory;

    @NotBlank(message = "Please Provide your medical allergies, if none write none")
    private String allergies;

    @NotBlank(message = "Please Provide a residence type")
    private String residenceType;
    
    @NotNull(message = "Please provide your height")
    @DecimalMin(value = "1.0",message = "Please Provide correct height")
    private Float height;
    
    @NotNull(message = "Please provide your weight")
    @DecimalMin(value = "1.0",message = "Please Provide correct weight")
    private Float weight;

    public MedicalDetailsDTO(MedicalDetails medicalDetails) {
        this.id = medicalDetails.getId();
        this.currentAddress = medicalDetails.getCurrentAddress();
        this.medicalHistory = medicalDetails.getMedicalHistory();
        this.familyMedicalHistory = medicalDetails.getFamilyMedicalHistory();
        this.allergies = medicalDetails.getAllergies();
        this.height = medicalDetails.getHeight();
        this.weight = medicalDetails.getWeight();
        this.residenceType = medicalDetails.getResidenceType();
    }
}
