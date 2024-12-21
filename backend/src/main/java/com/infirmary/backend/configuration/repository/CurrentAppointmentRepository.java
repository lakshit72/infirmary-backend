package com.infirmary.backend.configuration.repository;

import com.infirmary.backend.configuration.model.CurrentAppointment;
import com.infirmary.backend.configuration.model.Doctor;
import com.infirmary.backend.configuration.model.Location;

import java.util.Optional;
import java.util.UUID;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentAppointmentRepository extends JpaRepository<CurrentAppointment, UUID> {

    CurrentAppointment findByAppointment_AppointmentId(@NonNull UUID appointmentId);

    Optional<CurrentAppointment> findByPatient_Email(String email);

    Optional<CurrentAppointment> findByAppointment_Doctor_DoctorEmail(String doctorEmail);
    
    List<CurrentAppointment> findAllByAppointmentNotNullAndDoctorIsNull();

    Optional<CurrentAppointment> findByDoctor_DoctorEmail(String doctorEmail);

    Optional<CurrentAppointment> findByDoctor(Doctor doctor);

    int countByAppointmentNotNullAndAppointment_DateNot(LocalDate date);

    int countByAppointmentNotNull();

    List<CurrentAppointment> findAllByAppointmentNotNullAndDoctorNotNullAndAppointment_Location(Location location);
}
