package com.infirmary.backend.configuration.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infirmary.backend.configuration.model.ADPrescription;

public interface AdPrescriptionRepository extends JpaRepository<ADPrescription,UUID>{
    
}
