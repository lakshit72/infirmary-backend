package com.infirmary.backend.configuration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatientReqDTO {
    private String email;
    private String name;
    private String school;
    private String dateOfBirth;
    private String program;
    private String phoneNumber;
    private String emergencyContact;
    private String bloodGroup;
    private String img;
    private String password;
    private String gender;
    private String sapID;
}
