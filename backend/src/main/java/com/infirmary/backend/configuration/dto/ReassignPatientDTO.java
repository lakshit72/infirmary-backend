package com.infirmary.backend.configuration.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReassignPatientDTO {
    @NotBlank(message = "Patient Not Included")
    private String patientEmail;
    @NotNull(message = "Please Assign a doctor")
    private UUID doctorEmail;
}
