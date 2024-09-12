package com.infirmary.backend.configuration.model;

import com.infirmary.backend.configuration.dto.AdDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ad")
public class AD {
    @Id
    @Column(name = "ad_email",unique = true)
    private String adEmail;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password")
    private String password;

    public AD(AdDTO adDTO){
        this.adEmail = adDTO.getEmail();
        this.name = adDTO.getName();
        this.password = adDTO.getPassword();
    }
}
