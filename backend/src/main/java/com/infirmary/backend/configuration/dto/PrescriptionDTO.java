package com.infirmary.backend.configuration.dto;

import java.util.List;

import com.infirmary.backend.configuration.model.Doctor;
import com.infirmary.backend.configuration.model.Patient;
import com.infirmary.backend.configuration.model.Prescription;
import com.infirmary.backend.configuration.model.PrescriptionMeds;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PrescriptionDTO {
    private Patient patient;
    private Doctor doctor;
    private String diagnosis;
    private List<PrescriptionMedsDTORes> meds;
    private String dietaryRemarks;
    private String testNeeded;

    public PrescriptionDTO(Prescription prescription){
        this.diagnosis = prescription.getDiagnosis();
        this.doctor = prescription.getDoctor();
        this.patient = prescription.getPatient();
        this.dietaryRemarks = prescription.getDietaryRemarks();
        this.testNeeded = prescription.getTestNeeded();
        this.meds = prescription.getMedicine().stream().map(pres -> new PrescriptionMedsDTORes(pres)).toList();
    }
}

