package com.infirmary.backend.configuration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class PrescriptionRes {
    private PrescriptionDTO prescription;
    private LocalDate date;
}
