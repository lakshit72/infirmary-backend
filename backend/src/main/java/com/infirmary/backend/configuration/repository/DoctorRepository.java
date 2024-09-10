package com.infirmary.backend.configuration.repository;

import com.infirmary.backend.configuration.model.Doctor;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByDoctorId(@NonNull Long doctorId);
    Optional<Doctor> findByDoctorEmail(@NonNull String doctorEmail);
}
