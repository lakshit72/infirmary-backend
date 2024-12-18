package com.infirmary.backend.configuration.dto;

import com.infirmary.backend.configuration.model.PrescriptionMeds;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PrescriptionMedsDTORes {
    private StockDTO medicine;
    private Float dosageMorning;
    private Float dosageAfternoon;
    private Float dosageEvening;
    private int duration;
    private String suggestion; 

    public PrescriptionMedsDTORes(PrescriptionMeds prescriptionMeds){
        this.medicine = new StockDTO(prescriptionMeds.getMedicine());
        this.dosageAfternoon = prescriptionMeds.getDosageAfternoon();
        this.dosageEvening = prescriptionMeds.getDosageEvening();
        this.dosageMorning = prescriptionMeds.getDosageMorning();
        this.duration = prescriptionMeds.getDuration();
        this.suggestion = prescriptionMeds.getSuggestion();
    }
}
