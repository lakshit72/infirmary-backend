package com.infirmary.backend.configuration.model;

import com.infirmary.backend.configuration.dto.PatientDTO;
import com.infirmary.backend.configuration.dto.PatientReqDTO;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "patient")
public class Patient implements Serializable {
    @Id
    @Column(name = "sap_email", nullable = false,unique = true)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "school", nullable = false)
    private String school;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "program", nullable = false)
    private String program;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "emergency_contact", nullable = false)
    private String emergencyContact;

    @Column(name = "blood_group")
    private String bloodGroup;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "password")
    private String password;

    @Column(name = "gender")
    private String gender;
  
    @Column(name = "sap_id")
    private String sapId;

    public Patient(PatientDTO patientDTO){
        this.email = patientDTO.getEmail();
        this.name = patientDTO.getName();
        this.school = patientDTO.getSchool();
        this.dateOfBirth = patientDTO.getDateOfBirth();
        this.program = patientDTO.getProgram();
        this.phoneNumber = patientDTO.getPhoneNumber();
        this.emergencyContact = patientDTO.getEmergencyContact();
        this.bloodGroup = patientDTO.getBloodGroup();
        this.imageUrl = patientDTO.getImageUrl();
        this.password = patientDTO.getPassword();
        this.gender = patientDTO.getGender();
        this.sapId = patientDTO.getSapID();
    }
    public Patient(PatientReqDTO patientDTO){
        this.email = patientDTO.getEmail();
        this.name = patientDTO.getName();
        this.school = patientDTO.getSchool();
        this.dateOfBirth = patientDTO.getDateOfBirth();
        this.program = patientDTO.getProgram();
        this.phoneNumber = patientDTO.getPhoneNumber();
        this.emergencyContact = patientDTO.getEmergencyContact();
        this.bloodGroup = patientDTO.getBloodGroup();
        this.password = patientDTO.getPassword();
        this.gender = patientDTO.getGender();
        this.sapId = patientDTO.getSapID();
    }
}
