package com.infirmary.backend.configuration.dto;

import com.infirmary.backend.configuration.model.Doctor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DoctorDTO {
    private String doctorEmail;
    private String name;
    private Boolean status;
    private String gender;
    private String password;

    public DoctorDTO(Doctor doctor) {
        this.name = doctor.getName();
        this.status = doctor.isStatus();
        this.doctorEmail = doctor.getDoctorEmail();
        this.gender = doctor.getGender();
        this.password = doctor.getPassword();
    }
}
