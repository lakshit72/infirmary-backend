package com.infirmary.backend.configuration.repository;

import com.infirmary.backend.configuration.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByDoctorId(@NonNull Long doctorId);
}
