package com.infirmary.backend.configuration.model;

import com.infirmary.backend.configuration.dto.PrescriptionDTO;
import com.infirmary.backend.configuration.dto.PrescriptionReq;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "prescription")
@Getter
@Setter
@NoArgsConstructor
public class Prescription implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "prescription_id")
    private UUID prescriptionId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sap_email", referencedColumnName = "sap_email")
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_email", referencedColumnName = "doctor_email")
    private Doctor doctor;

    @Column(name = "diagnosis")
    private String diagnosis;

    @Column(name = "dietary_remarks")
    private String dietaryRemarks;

    @Column(name = "test_needed")
    private String testNeeded;

    @OneToMany(mappedBy = "prescription",cascade = CascadeType.ALL,orphanRemoval = false)
    private List<PrescriptionMeds> medicine = new ArrayList<>();

    public Prescription(PrescriptionDTO prescriptionDTO) {
        this.diagnosis = prescriptionDTO.getDiagnosis();
        this.dietaryRemarks = prescriptionDTO.getDietaryRemarks();
        this.testNeeded = prescriptionDTO.getTestNeeded();
    }

    public Prescription(PrescriptionReq prescriptionDTO) {
        this.diagnosis = prescriptionDTO.getDiagnosis();
        this.dietaryRemarks = prescriptionDTO.getDietaryRemarks();
        this.testNeeded = prescriptionDTO.getTestNeeded();
    }

    public void addPresMed(PrescriptionMeds prescriptionMeds){
        prescriptionMeds.setPrescription(this);
        medicine.add(prescriptionMeds);
    }
}
