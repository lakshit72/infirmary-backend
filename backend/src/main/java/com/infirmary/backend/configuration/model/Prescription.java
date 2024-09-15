package com.infirmary.backend.configuration.model;

import com.infirmary.backend.configuration.dto.PrescriptionDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "prescription")
@Getter
@Setter
@NoArgsConstructor
public class Prescription implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prescription_id")
    private Long prescriptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sap_email", referencedColumnName = "sap_email")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_email", referencedColumnName = "doctor_email")
    private Doctor doctor;

    @Column(name = "diagnosis")
    private String diagnosis;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Stock> stock;

    @Column(name = "dosage")
    private Long dosage;

    @Column(name = "duration")
    private Long duration;

    @Column(name = "suggestion")
    private String suggestion;

    @Column(name = "dietary_remarks")
    private String dietaryRemarks;

    @Column(name = "test_needed")
    private String testNeeded;

    public Prescription(PrescriptionDTO prescriptionDTO) {
        this.prescriptionId = prescriptionDTO.getPrescriptionId();
        this.patient = new Patient(prescriptionDTO.getPatientDTO());
        this.doctor = new Doctor(prescriptionDTO.getDoctorDTO());
        this.diagnosis = prescriptionDTO.getDiagnosis();
        this.dosage = prescriptionDTO.getDosage();
        this.suggestion = prescriptionDTO.getSuggestion();
        this.dietaryRemarks = prescriptionDTO.getDietaryRemarks();
        this.testNeeded = prescriptionDTO.getTestNeeded();
    }
}
