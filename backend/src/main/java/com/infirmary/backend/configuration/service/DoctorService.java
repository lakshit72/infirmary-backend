package com.infirmary.backend.configuration.service;

import com.infirmary.backend.configuration.Exception.DoctorNotFoundException;
import com.infirmary.backend.configuration.dto.DoctorDTO;
import org.springframework.stereotype.Service;

@Service
public interface DoctorService {
    DoctorDTO getDoctorById(Long id) throws DoctorNotFoundException;
}
