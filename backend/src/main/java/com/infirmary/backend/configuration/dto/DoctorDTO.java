package com.infirmary.backend.configuration.dto;

import com.infirmary.backend.configuration.model.Doctor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DoctorDTO {
    private Long doctorId;
    private String name;
    private String email;
    private Boolean status;
    private String gender;
    private String password;

    public DoctorDTO(Doctor doctor) {
        this.doctorId = doctor.getDoctorId();
        this.name = doctor.getName();
        this.email = doctor.getDoctorEmail();
        this.status = doctor.isStatus();
        this.gender = doctor.getGender();
        this.password = doctor.getPassword();
    }
}
