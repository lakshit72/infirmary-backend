package com.infirmary.backend.configuration.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "patient_medical_details_mapping")
@NoArgsConstructor
public class PatientMedicalDetailsMapping implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "sap_email", referencedColumnName = "sap_email", nullable = false)
    private Patient patient;

    @OneToOne
    @JoinColumn(name = "medical_details_id", referencedColumnName = "id", nullable = false)
    private MedicalDetails medicalDetails;

    public PatientMedicalDetailsMapping(Patient patient, MedicalDetails medicalDetails){
        this.patient = patient;
        this.medicalDetails = medicalDetails;
    }
}
