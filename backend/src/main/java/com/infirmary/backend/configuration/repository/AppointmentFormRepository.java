package com.infirmary.backend.configuration.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.infirmary.backend.configuration.model.AppointmentForm;

@Repository
public interface AppointmentFormRepository extends JpaRepository<AppointmentForm,UUID> {
    
}
