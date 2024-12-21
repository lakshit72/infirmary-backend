package com.infirmary.backend.configuration.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.infirmary.backend.configuration.model.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin,String>{
    Optional<Admin> findByAdminEmail(String adminEmail);
    Boolean existsByAdminEmail(String adminEmail);
}
