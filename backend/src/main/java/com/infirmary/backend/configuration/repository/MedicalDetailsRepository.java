package com.infirmary.backend.configuration.repository;

import com.infirmary.backend.configuration.model.MedicalDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface MedicalDetailsRepository extends JpaRepository<MedicalDetails, Long> {

    MedicalDetails findByPatient_SapId(@NonNull Long sapId);
}
