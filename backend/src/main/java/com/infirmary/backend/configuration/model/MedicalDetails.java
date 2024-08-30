package com.infirmary.backend.configuration.model;

import com.infirmary.backend.configuration.dto.MedicalDetailsDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "medical_details")
@NoArgsConstructor
public class MedicalDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "blood_group", nullable = false)
    private String bloodGroup;

    @Column(name = "medical_history", nullable = false)
    private String medicalHistory;

    @Column(name = "family_medical_history", nullable = false)
    private String familyMedicalHistory;

    @Column(name = "allergies", nullable = false)
    private String allergies;

    public MedicalDetails(MedicalDetailsDTO medicalDetailsDTO){
        this.id = medicalDetailsDTO.getId();
        this.gender = medicalDetailsDTO.getGender();
        this.bloodGroup = medicalDetailsDTO.getBloodGroup();
        this.medicalHistory = medicalDetailsDTO.getMedicalHistory();
        this.familyMedicalHistory = medicalDetailsDTO.getFamilyMedicalHistory();
        this.allergies = medicalDetailsDTO.getAllergies();
    }
}
