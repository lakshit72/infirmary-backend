package com.infirmary.backend.configuration.model;

import java.io.Serializable;

import com.infirmary.backend.configuration.dto.AdDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ad")
public class AD implements Serializable{
    @Id
    @Column(name = "ad_email",unique = true)
    private String adEmail;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "designation")
    private String designation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location")
    private Location location;

    public AD(AdDTO adDTO){
        this.adEmail = adDTO.getEmail();
        this.name = adDTO.getName();
        this.password = adDTO.getPassword();
        this.designation = adDTO.getDesignation();
    }
}
