package com.infirmary.backend.configuration.repository;

import com.infirmary.backend.configuration.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Optional<Appointment> findByAppointmentId(@NonNull Long appointmentId);

    List<Appointment> findByPatient_SapId(@NonNull Long sapId);

    List<Appointment> findByDoctor_DoctorId(@NonNull Long doctorId);
}
