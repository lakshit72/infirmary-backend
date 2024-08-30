package com.infirmary.backend.configuration.dto;

import com.infirmary.backend.configuration.model.MedicalDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MedicalDetailsDTO {
    private Long id;
    private String gender;
    private String bloodGroup;
    private String medicalHistory;
    private String familyMedicalHistory;
    private String allergies;

    public MedicalDetailsDTO(MedicalDetails medicalDetails){
        this.id = medicalDetails.getId();
        this.gender = medicalDetails.getGender();
        this.bloodGroup = medicalDetails.getBloodGroup();
        this.medicalHistory = medicalDetails.getMedicalHistory();
        this.familyMedicalHistory = medicalDetails.getFamilyMedicalHistory();
        this.allergies = medicalDetails.getAllergies();
    }
}
