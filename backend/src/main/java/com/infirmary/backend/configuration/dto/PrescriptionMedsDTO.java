package com.infirmary.backend.configuration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PrescriptionMedsDTO {
    private Long medicine;
    private int dosage;
    private int duration;
    private String suggestion; 
}
