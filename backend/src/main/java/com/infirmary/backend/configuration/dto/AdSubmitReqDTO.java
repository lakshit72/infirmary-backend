package com.infirmary.backend.configuration.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class AdSubmitReqDTO {
    @DecimalMin(value="1.0" , message = "Please enter weight")
    private Float weight;

    @DecimalMin(value="1.0" , message = "Please enter temperature")
    private Float temperature;

    @NotBlank(message = "Please assign doctor")
    private Long doctorAss;

    @NotBlank(message = "Please assign patient")
    private String patEmail;
}
