package com.infirmary.backend.configuration.service;

import com.infirmary.backend.configuration.dto.DoctorDTO;
import com.infirmary.backend.configuration.Exception.DoctorNotFoundException;

public interface DoctorService {
    DoctorDTO getDoctorById(Long doctorId) throws DoctorNotFoundException;
    DoctorDTO createDoctor(DoctorDTO doctorDTO);
    void validateDoctorData(DoctorDTO doctorDTO);
}
