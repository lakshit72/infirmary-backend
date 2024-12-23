package com.infirmary.backend.configuration.dto;

import java.util.UUID;

import com.infirmary.backend.configuration.model.Doctor;
import com.infirmary.backend.configuration.model.Location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DoctorNoPassDTO {
    private UUID doctorId;
    private String doctorEmail;
    private String name;
    private Boolean status;
    private String gender;
    private String designation;
    private Location location;

    public DoctorNoPassDTO(Doctor doctor) {
        this.doctorId = doctor.getDoctorId();
        this.name = doctor.getName();
        this.status = doctor.isStatus();
        this.doctorEmail = doctor.getDoctorEmail();
        this.gender = doctor.getGender();
        this.location = doctor.getLocation();
        this.designation = doctor.getDesignation();
    }
}
