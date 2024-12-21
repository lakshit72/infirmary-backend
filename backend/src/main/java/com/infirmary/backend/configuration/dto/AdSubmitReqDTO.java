package com.infirmary.backend.configuration.dto;

import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class AdSubmitReqDTO {
    @NotNull(message = "Please Assign weight")
    @DecimalMin(value="1.0" , message = "Please enter weight")
    private Float weight;

    @NotNull(message = "Please Assign temperature")
    @DecimalMin(value="1.0" , message = "Please enter temperature")
    private Float temperature;

    @NotNull(message = "Please assign doctor")
    private UUID doctorAss;

    @NotBlank(message = "Please assign patient")
    private String patEmail;
}
