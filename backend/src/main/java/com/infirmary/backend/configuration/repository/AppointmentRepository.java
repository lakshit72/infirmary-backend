package com.infirmary.backend.configuration.repository;

import com.infirmary.backend.configuration.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Appointment findByAppointmentId(@NonNull Long appointmentId);

    Appointment findByPrescriptionURL(@NonNull String prescriptionURL);

    List<Appointment> findByDoctor_DoctorId(Long doctorId);

    Optional<Appointment> findFirstByPatient_EmailOrderByDateDesc(String email);

    List<Appointment> findByPatient_Email(@NonNull String email);

    List<Appointment> findByDate(LocalDate date);
}
