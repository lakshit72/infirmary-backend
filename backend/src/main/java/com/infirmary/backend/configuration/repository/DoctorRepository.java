package com.infirmary.backend.configuration.repository;

import com.infirmary.backend.configuration.model.Doctor;
import com.infirmary.backend.configuration.model.Location;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
    Optional<Doctor> findByDoctorEmail(@NonNull String doctorEmail);
    Boolean existsByDoctorEmail(@NonNull String doctorEmail);
    List<Doctor> findByStatusTrue();
    List<Doctor> findByStatusTrueAndLocation(Location location);
}
