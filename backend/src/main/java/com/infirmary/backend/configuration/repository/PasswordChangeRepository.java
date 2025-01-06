package com.infirmary.backend.configuration.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infirmary.backend.configuration.model.AD;
import com.infirmary.backend.configuration.model.Doctor;
import com.infirmary.backend.configuration.model.PasswordChange;
import com.infirmary.backend.configuration.model.Patient;

public interface PasswordChangeRepository extends JpaRepository<PasswordChange,UUID>{
    Optional<PasswordChange> findByPatient(Patient patient);
    Optional<PasswordChange> findByAd(AD ad);
    Optional<PasswordChange> findByDoctor(Doctor doctor);
}
