package com.infirmary.backend.configuration.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PrescriptionReq {
    private List<PrescriptionMedsDTO> meds;
    private String diagnosis;
    private String dietaryRemarks;
    private String testNeeded;
}
