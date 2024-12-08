package com.infirmary.backend.configuration.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminDTO {
    private String name;
    private String email;
    private String password;
}
