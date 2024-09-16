package com.infirmary.backend.configuration.repository;

import com.infirmary.backend.configuration.model.Patient;
import com.infirmary.backend.configuration.model.Prescription;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByPatient(Patient patient);
}
