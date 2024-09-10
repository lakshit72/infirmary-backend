package com.infirmary.backend.configuration.repository;

import com.infirmary.backend.configuration.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByEmail(@NonNull String email);

    Boolean existsByEmail(String Email);
}
