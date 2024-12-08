package com.infirmary.backend.configuration.dto;

import com.infirmary.backend.configuration.model.PrescriptionMeds;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrescriptionMedsDTORes {
    private StockDTO medicine;
    private DosageDTO dosage;
    private int duration;
    private String suggestion; 

    public PrescriptionMedsDTORes(PrescriptionMeds prescriptionMeds){
        this.medicine = new StockDTO(prescriptionMeds.getMedicine());
        this.dosage.setA(prescriptionMeds.getDosageAfternoon());
        this.dosage.setE(prescriptionMeds.getDosageEvening());
        this.dosage.setM(prescriptionMeds.getDosageMorning());
        this.duration = prescriptionMeds.getDuration();
        this.suggestion = prescriptionMeds.getSuggestion();
    }
}
