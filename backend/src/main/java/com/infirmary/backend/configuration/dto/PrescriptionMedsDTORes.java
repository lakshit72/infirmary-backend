package com.infirmary.backend.configuration.dto;

import com.infirmary.backend.configuration.model.PrescriptionMeds;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrescriptionMedsDTORes {
    private StockDTO medicine;
    private int dosage;
    private int duration;
    private String suggestion; 

    public PrescriptionMedsDTORes(PrescriptionMeds prescriptionMeds){
        this.medicine = new StockDTO(prescriptionMeds.getMedicine());
        this.dosage = prescriptionMeds.getDosage();
        this.duration = prescriptionMeds.getDuration();
        this.suggestion = prescriptionMeds.getSuggestion();
    }
}
