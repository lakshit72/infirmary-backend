package com.infirmary.backend.configuration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class AdSubmitReqDTO {
    private Float weight;
    private Float temperature;
    private Long doctorAss;
    private String patEmail;
}
