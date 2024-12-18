package com.infirmary.backend.configuration.model;

import com.infirmary.backend.configuration.dto.MedicalDetailsDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "medical_details")
@NoArgsConstructor
public class MedicalDetails implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sap_email", referencedColumnName = "sap_email")
    private Patient patient;

    @Column(name = "current_address", nullable = false)
    private String currentAddress;

    @Column(name = "medical_history", nullable = false)
    private String medicalHistory;

    @Column(name = "family_medical_history", nullable = false)
    private String familyMedicalHistory;

    @Column(name = "allergies", nullable = false)
    private String allergies;

    @Column(name = "height", nullable = false)
    private Float height;

    @Column(name = "weight", nullable = false)
    private Float weight;

    public MedicalDetails(MedicalDetailsDTO medicalDetailsDTO){
        this.id = medicalDetailsDTO.getId();
        this.currentAddress = medicalDetailsDTO.getCurrentAddress();
        this.medicalHistory = medicalDetailsDTO.getMedicalHistory();
        this.familyMedicalHistory = medicalDetailsDTO.getFamilyMedicalHistory();
        this.allergies = medicalDetailsDTO.getAllergies();
        this.height = medicalDetailsDTO.getHeight();
        this.weight = medicalDetailsDTO.getWeight();
    }

    public void updateFromMedicalDetailsDTO(MedicalDetailsDTO medicalDetailsDTO){
        this.currentAddress = medicalDetailsDTO.getCurrentAddress();
        this.medicalHistory = medicalDetailsDTO.getMedicalHistory();
        this.familyMedicalHistory = medicalDetailsDTO.getFamilyMedicalHistory();
        this.allergies = medicalDetailsDTO.getAllergies();
        this.height = medicalDetailsDTO.getHeight();
        this.weight = medicalDetailsDTO.getWeight();
    }
}
