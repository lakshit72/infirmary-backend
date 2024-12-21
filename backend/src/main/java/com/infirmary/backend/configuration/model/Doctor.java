package com.infirmary.backend.configuration.model;

import com.infirmary.backend.configuration.dto.DoctorDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "doctor")
public class Doctor implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "doctor_id")
    private UUID doctorId;

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

    @Column(name = "designation")
    private String designation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location")
    private Location location;

    public Doctor(DoctorDTO doctorDTO) {
        this.doctorEmail = doctorDTO.getDoctorEmail();
        this.status = doctorDTO.getStatus();
        this.name = doctorDTO.getName();
        this.gender = doctorDTO.getGender();
        this.password = doctorDTO.getPassword();
        this.designation = doctorDTO.getDesignation();
    }
}