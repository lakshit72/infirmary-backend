package com.infirmary.backend.configuration.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.infirmary.backend.configuration.model.AD;

@Repository
public interface AdRepository extends JpaRepository<AD,String>{
    Optional<AD> findByAdEmail(String adEmail);
}
