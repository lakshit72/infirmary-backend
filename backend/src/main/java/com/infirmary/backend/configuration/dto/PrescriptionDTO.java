package com.infirmary.backend.configuration.dto;

import java.util.List;

import com.infirmary.backend.configuration.model.Prescription;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PrescriptionDTO {
    private PatientNoPassDTO patient;
    private DoctorNoPassDTO doctor;
    private String diagnosis;
    private List<PrescriptionMedsDTORes> meds;
    private String dietaryRemarks;
    private String testNeeded;

    public PrescriptionDTO(Prescription prescription){
        this.diagnosis = prescription.getDiagnosis();
        this.doctor = new DoctorNoPassDTO(prescription.getDoctor());
        this.patient = new PatientNoPassDTO(prescription.getPatient());
        this.dietaryRemarks = prescription.getDietaryRemarks();
        this.testNeeded = prescription.getTestNeeded();
        this.meds = prescription.getMedicine().stream().map(pres -> new PrescriptionMedsDTORes(pres)).toList();
    }
}

