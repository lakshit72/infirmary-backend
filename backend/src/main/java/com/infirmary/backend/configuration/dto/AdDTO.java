package com.infirmary.backend.configuration.dto;

import com.infirmary.backend.configuration.model.AD;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdDTO {
    private String email;
    private String password;
    private String name;

    public AdDTO(AD ad){
        this.email = ad.getAdEmail();
        this.name = ad.getName();
        this.password = ad.getPassword();
    }
}
