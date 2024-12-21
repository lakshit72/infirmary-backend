package com.infirmary.backend.configuration.repository;

import com.infirmary.backend.configuration.model.MedicalDetails;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalDetailsRepository extends JpaRepository<MedicalDetails, UUID> {
    Optional<MedicalDetails> findByPatient_Email(@NonNull String email);
}
