package com.infirmary.backend.configuration.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infirmary.backend.configuration.model.Conformation;

public interface ConformationRepository extends JpaRepository<Conformation,UUID>{
    Boolean existsByPatient_Email(String email);
    Boolean existsByDoctor_DoctorEmail(String email);
    Boolean existsByAd_AdEmail(String email);
}
