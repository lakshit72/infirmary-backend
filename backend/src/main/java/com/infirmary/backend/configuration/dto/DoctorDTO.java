package com.infirmary.backend.configuration.dto;

import java.util.UUID;

import com.infirmary.backend.configuration.model.Doctor;
import com.infirmary.backend.configuration.model.Location;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DoctorDTO {
    private UUID doctorId;
    @NotBlank(message = "Email must not be empty")
    private String doctorEmail;
    @NotBlank(message = "Name must not be empty")
    private String name;
    private Boolean status;
    @NotBlank(message = "Gender must not be empty")
    private String gender;
    @NotBlank(message = "Password must not be empty")
    private String password;
    @NotBlank(message = "Designation must not be empty")
    private String designation;
    private Location location;

    public DoctorDTO(Doctor doctor) {
        this.doctorId = doctor.getDoctorId();
        this.name = doctor.getName();
        this.status = doctor.isStatus();
        this.doctorEmail = doctor.getDoctorEmail();
        this.gender = doctor.getGender();
        this.password = doctor.getPassword();
        this.location = doctor.getLocation();
        this.designation = doctor.getDesignation();
    }
}
