package com.infirmary.backend.configuration.repository;

import com.infirmary.backend.configuration.model.Doctor;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByDoctorEmail(@NonNull String doctorEmail);
    Boolean existsByDoctorEmail(@NonNull String doctorEmail);
    List<Doctor> findByStatusTrue();
}
