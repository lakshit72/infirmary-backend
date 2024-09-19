package com.infirmary.backend.configuration.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infirmary.backend.configuration.model.PrescriptionMeds;

public interface PrescriptionMedsRepository extends JpaRepository<PrescriptionMeds,Long>{
    
}
