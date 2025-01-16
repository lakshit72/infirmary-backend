package com.infirmary.backend.configuration.dto;

import java.util.List;

import com.infirmary.backend.configuration.model.Prescription;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrescriptionInfo {
    private String diagnosis;
    private List<PrescriptionMedsDTORes> medicine;
    private String recommendation;
    private String testNeeded;

    public PrescriptionInfo(Prescription prescription){
        this.diagnosis = prescription.getDiagnosis();
        this.recommendation = prescription.getDietaryRemarks();
        this.testNeeded = prescription.getTestNeeded();
        this.medicine = prescription.getMedicine().stream().map(medicine -> new PrescriptionMedsDTORes(medicine)).toList();
    }
}
