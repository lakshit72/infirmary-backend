package com.infirmary.backend.configuration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.infirmary.backend.configuration.model.PrescriptionMeds;

@Repository
public interface PrescriptionMedsRepository extends JpaRepository<PrescriptionMeds,Long>{
    
}
