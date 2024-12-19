package com.infirmary.backend.configuration.dto;

import com.infirmary.backend.configuration.model.AD;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdDTO {
    private String email;
    private String password;
    private String name;
    private String designation;

    public AdDTO(AD ad){
        this.email = ad.getAdEmail();
        this.name = ad.getName();
        this.password = ad.getPassword();
    }
}
