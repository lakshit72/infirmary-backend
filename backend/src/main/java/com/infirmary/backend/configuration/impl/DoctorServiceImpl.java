package com.infirmary.backend.configuration.impl;

import com.infirmary.backend.configuration.dto.DoctorDTO;
import com.infirmary.backend.configuration.Exception.DoctorNotFoundException;
import com.infirmary.backend.configuration.model.Doctor;
import com.infirmary.backend.configuration.repository.DoctorRepository;
import com.infirmary.backend.configuration.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public DoctorDTO getDoctorById(Long doctorId) throws DoctorNotFoundException {
        Optional<Doctor> doctor = doctorRepository.findByDoctorId(doctorId);
        if (doctor.isPresent()) {
            return new DoctorDTO(doctor.get());
        } else {
            throw new DoctorNotFoundException("Doctor not found with ID: " + doctorId);
        }
    }

    @Override
    public DoctorDTO createDoctor(DoctorDTO doctorDTO) {
        validateDoctorData(doctorDTO);
        Doctor doctor = new Doctor(doctorDTO.getName(), doctorDTO.getGender());
        Doctor savedDoctor = doctorRepository.save(doctor);
        return new DoctorDTO(savedDoctor);
    }

    @Override
    public void validateDoctorData(DoctorDTO doctorDTO) {
        if (doctorDTO.getName() == null || doctorDTO.getGender() == null) {
            throw new IllegalArgumentException("Doctor name and gender must not be null");
        }
    }
}
