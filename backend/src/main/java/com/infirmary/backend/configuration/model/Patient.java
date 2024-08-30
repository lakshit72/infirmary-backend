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
    @Column(name = "sap_id", nullable = false)
    private Long sapId;

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

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "emergency_contact", nullable = false)
    private String emergencyContact;

    public Patient(PatientDTO patientDTO){
        this.sapId = patientDTO.getSapId();
        this.name = patientDTO.getName();
        this.school = patientDTO.getSchool();
        this.dateOfBirth = patientDTO.getDateOfBirth();
        this.program = patientDTO.getProgram();
        this.phoneNumber = patientDTO.getPhoneNumber();
        this.address = patientDTO.getAddress();
        this.emergencyContact = patientDTO.getEmergencyContact();
    }
}
