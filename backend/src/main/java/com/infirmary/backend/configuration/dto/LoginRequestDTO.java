package com.infirmary.backend.configuration.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    @NotBlank(message = "Must Provide an email")
    private String email;
    @NotBlank(message = "Must provide a password")
    private String password;

    public LoginRequestDTO(String email,String password){
        this.email = email;
        this.password = password;
    }
}
