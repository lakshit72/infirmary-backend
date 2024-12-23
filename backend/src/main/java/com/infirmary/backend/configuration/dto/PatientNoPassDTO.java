package com.infirmary.backend.configuration.dto;

import java.time.LocalDate;

import com.infirmary.backend.configuration.model.Patient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatientNoPassDTO {
    private String email;
    private String name;
    private String school;
    private LocalDate dateOfBirth;
    private String program;
    private String phoneNumber;
    private String emergencyContact;
    private String bloodGroup;
    private String imageUrl;
    private String gender;
    private String sapID;

    public PatientNoPassDTO(Patient patient) {
        this.email = patient.getEmail();
        this.name = patient.getName();
        this.school = patient.getSchool();
        this.dateOfBirth = patient.getDateOfBirth();
        this.program = patient.getProgram();
        this.phoneNumber = patient.getPhoneNumber();
        this.emergencyContact = patient.getEmergencyContact();
        this.bloodGroup = patient.getBloodGroup();
        this.imageUrl = patient.getImageUrl();
        this.gender = patient.getGender();
        this.sapID = patient.getSapId();
    }
}
