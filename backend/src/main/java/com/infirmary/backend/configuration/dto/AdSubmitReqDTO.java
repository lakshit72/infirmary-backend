package com.infirmary.backend.configuration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdSubmitReqDTO {
    private float weight;
    private float temperature;
    private Long doctorAss;
    private String patEmail;
}
