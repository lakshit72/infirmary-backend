package com.infirmary.backend.configuration.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PrescriptionReq {
    private List<PrescriptionMedsDTO> meds;

    @NotBlank(message = "Please provide diagnosis")
    private String diagnosis;

    @NotBlank(message = "Please provide dietary remarks")
    private String dietaryRemarks;

    @NotBlank(message = "Please provide tests needed")
    private String testNeeded;
}
