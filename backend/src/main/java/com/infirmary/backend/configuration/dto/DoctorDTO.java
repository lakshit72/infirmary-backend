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
    private String gender;

    public DoctorDTO(Doctor doctor) {
        this.doctorId = doctor.getDoctorId();
        this.name = doctor.getName();
        this.gender = doctor.getGender();
    }
}
