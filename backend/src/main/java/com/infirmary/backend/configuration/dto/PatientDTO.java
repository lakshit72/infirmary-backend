package com.infirmary.backend.configuration.dto;

import com.infirmary.backend.configuration.model.Patient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.nio.file.SecureDirectoryStream;

@Getter
@Setter
@NoArgsConstructor
public class PatientDTO {
    private String sapEmail;
    private String name;
    private String school;
    private String dateOfBirth;
    private String program;
    private String phoneNumber;
    private String address;
    private String emergencyContact;
    private String bloodGroup;
    private String imageUrl;

    public PatientDTO(Patient patient){
        this.sapEmail = patient.getSapEmail();
        this.name = patient.getName();
        this.school = patient.getSchool();
        this.dateOfBirth = patient.getDateOfBirth();
        this.program = patient.getProgram();
        this.phoneNumber = patient.getPhoneNumber();
        this.address = patient.getAddress();
        this.emergencyContact = patient.getEmergencyContact();
        this.bloodGroup = patient.getBloodGroup();
        this.imageUrl = patient.getImageUrl();
    }
}
