package com.infirmary.backend.configuration.dto;

import com.infirmary.backend.configuration.model.Patient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatientDTO {
    private String email;
    private String name;
    private String school;
    private String dateOfBirth;
    private String program;
    private String phoneNumber;
    private String emergencyContact;
    private String bloodGroup;
    private String imageUrl;
    private String password;

    public PatientDTO(Patient patient) {
        this.email = patient.getEmail();
        this.name = patient.getName();
        this.school = patient.getSchool();
        this.dateOfBirth = patient.getDateOfBirth();
        this.program = patient.getProgram();
        this.phoneNumber = patient.getPhoneNumber();
        this.emergencyContact = patient.getEmergencyContact();
        this.bloodGroup = patient.getBloodGroup();
        this.imageUrl = patient.getImageUrl();
        this.password = patient.getPassword();
    }
}
