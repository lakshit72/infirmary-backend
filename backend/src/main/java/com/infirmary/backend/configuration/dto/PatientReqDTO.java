package com.infirmary.backend.configuration.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatientReqDTO {
    @NotBlank(message = "Please Provide your Email")
    private String email;
    
    @NotBlank(message = "Please Provide your Name")
    private String name;
    
    @NotBlank(message = "Please Provide your school")
    private String school;
    
    @NotNull(message = "Please Provide a Valid DOB")
    @Past(message = "Please Provide your Date of Birth")
    private LocalDate dateOfBirth;
    
    @NotBlank(message = "Please provide your program")
    private String program;
    
    @NotBlank(message = "Please Provide a phone number")
    @Size(min = 10,max = 10,message = "Please Provide a valid number")
    private String phoneNumber;
    
    @NotBlank(message = "Please Provide Parent Contact Contact")
    @Size(min = 10,max = 10,message = "Please Provide a valid number")
    private String emergencyContact;

    @NotBlank(message = "Please provide a valid blood group")
    private String bloodGroup;

    private String img;
    
    @NotBlank(message = "Password must not be empty")
    private String password;

    @NotBlank(message = "Please provide a gender")
    private String gender;

    @NotBlank(message = "Please Provide your sapID")
    @Size(min = 8, max = 9,message = "Please Provide a Valid Sap ID")
    private String sapID;
}
