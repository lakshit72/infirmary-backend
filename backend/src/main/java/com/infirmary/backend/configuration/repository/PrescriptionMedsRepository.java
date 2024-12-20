package com.infirmary.backend.configuration.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.infirmary.backend.configuration.model.PrescriptionMeds;
import com.infirmary.backend.configuration.model.Stock;

@Repository
public interface PrescriptionMedsRepository extends JpaRepository<PrescriptionMeds,UUID>{
    Boolean existsByStock(Stock stock);
}
