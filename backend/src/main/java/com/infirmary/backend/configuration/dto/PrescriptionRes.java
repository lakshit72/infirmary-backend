package com.infirmary.backend.configuration.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class PrescriptionRes {
    private PrescriptionDTO prescription;
    private LocalDate date;
    private String time;
    private String residenceType;

}
