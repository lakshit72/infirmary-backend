package com.infirmary.backend.configuration.dto;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdHocSubmitDTO {
    @NotBlank(message = "Please provide a name")
    private String name;
    @NotBlank(message = "Please provide a email")
    private String patientEmail;
    @NotNull(message = "No medicine assigned")
    private UUID medicine;
    @NotNull(message = "Please provide a quantity")
    @Min(value = 1,message = "Quantity must be atleast 1")
    private Integer quantity;
}
