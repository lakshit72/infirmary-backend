package com.infirmary.backend.configuration.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PrescriptionMedsDTO {
    private UUID medicine;
    private Float dosageMorning;
    private Float dosageAfternoon;
    private Float dosageEvening;
    private int duration;
    private String suggestion; 
}
