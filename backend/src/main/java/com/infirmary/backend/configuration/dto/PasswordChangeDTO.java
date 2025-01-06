package com.infirmary.backend.configuration.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PasswordChangeDTO {
    @NotBlank(message = "Please Provide a Password")
    private String newPass;
    @NotBlank(message = "Please enter the password again")
    private String repeatPassword;
}
