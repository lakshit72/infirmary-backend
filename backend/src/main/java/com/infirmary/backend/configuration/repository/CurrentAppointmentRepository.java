package com.infirmary.backend.configuration.repository;

import com.infirmary.backend.configuration.model.CurrentAppointment;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentAppointmentRepository extends JpaRepository<CurrentAppointment, Long> {

    CurrentAppointment findByAppointment_AppointmentId(@NonNull Long appointmentId);

    Optional<CurrentAppointment> findByPatient_Email(String email);

    Optional<CurrentAppointment> findByAppointment_Doctor_DoctorEmail(String doctorEmail);
    
    List<CurrentAppointment> findAllByAppointmentNotNullAndDoctorIsNull();

    Optional<CurrentAppointment> findByDoctor_DoctorEmail(String doctorEmail);
}
