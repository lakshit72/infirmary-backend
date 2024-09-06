package com.infirmary.backend.configuration.repository;

import com.infirmary.backend.configuration.model.MedicalDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalDetailsRepository extends JpaRepository<MedicalDetails, Long> {
    MedicalDetails findByPatient_Email(@NonNull String email);
}
