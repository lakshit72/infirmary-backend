package com.infirmary.backend.configuration.model;

import com.infirmary.backend.configuration.dto.PatientDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "patient")
public class Patient implements Serializable {
    @Id
    @Column(name = "sap_email", nullable = false)
    private String sapEmail;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "school", nullable = false)
    private String school;

    @Column(name = "date_of_birth", nullable = false)
    private String dateOfBirth;

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

    public Patient(PatientDTO patientDTO){
        this.sapEmail = patientDTO.getSapEmail();
        this.name = patientDTO.getName();
        this.school = patientDTO.getSchool();
        this.dateOfBirth = patientDTO.getDateOfBirth();
        this.program = patientDTO.getProgram();
        this.phoneNumber = patientDTO.getPhoneNumber();
        this.emergencyContact = patientDTO.getEmergencyContact();
        this.bloodGroup = patientDTO.getBloodGroup();
        this.imageUrl = patientDTO.getImageUrl();

    }
}
