package com.infirmary.backend.configuration.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientDetailsResponseDTO {
    private String currentAddress;
    private String medicalHistory;
    private String familyMedicalHistory;
    private String residenceType;
    private String allergies;
    private Float height;
    private Float weight;
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
    
    public PatientDetailsResponseDTO(PatientDTO patient, MedicalDetailsDTO medicalDetails) {
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
        this.sapID = patient.getSapID();
        this.currentAddress = medicalDetails.getCurrentAddress();
        this.medicalHistory = medicalDetails.getMedicalHistory();
        this.familyMedicalHistory = medicalDetails.getFamilyMedicalHistory();
        this.allergies = medicalDetails.getAllergies();
        this.height = medicalDetails.getHeight();
        this.weight = medicalDetails.getWeight();
        this.residenceType = medicalDetails.getResidenceType();
    }
}
