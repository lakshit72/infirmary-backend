package com.infirmary.backend.configuration.repository;

import com.infirmary.backend.configuration.model.CurrentAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface CurrentAppointmentRepository extends JpaRepository<CurrentAppointment, Long> {

    CurrentAppointment findByAppointment_AppointmentId(@NonNull Long appointmentId);

    CurrentAppointment findByPatient_Email(String email);
}
