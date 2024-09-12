package com.infirmary.backend.configuration.repository;

import com.infirmary.backend.configuration.model.DoctorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorStatusRepository extends JpaRepository<DoctorStatus, Long> {

    DoctorStatus findByDoctor_DoctorEmail(String doctorId);
}
