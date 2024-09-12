package com.infirmary.backend.configuration.repository;

import com.infirmary.backend.configuration.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

}
