package com.infirmary.backend.configuration.model;

import com.infirmary.backend.configuration.dto.DoctorDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "doctor")
public class Doctor implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Long doctorId;
    
    @Column(name = "doctor_email",unique = true)
    private String doctorEmail;
    
    @Column(name = "status",nullable = true)
    private boolean status;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "gender", nullable = false)
    private String gender;
    
    @Column(name = "password")
    private String password;

    public Doctor(DoctorDTO doctorDTO) {
        this.doctorEmail = doctorDTO.getDoctorEmail();
        this.status = doctorDTO.getStatus();
        this.name = doctorDTO.getName();
        this.gender = doctorDTO.getGender();
        this.password = doctorDTO.getPassword();
    
    }
}