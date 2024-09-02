package com.infirmary.backend.configuration.repository;

import com.infirmary.backend.configuration.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {


    Optional<Patient> findBySapId(@NonNull Long id);
}
