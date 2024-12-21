package com.infirmary.backend.configuration.repository;

import com.infirmary.backend.configuration.model.Appointment;
import com.infirmary.backend.configuration.model.Location;
import com.infirmary.backend.configuration.model.Patient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    Appointment findByAppointmentId(@NonNull UUID appointmentId);

    List<Appointment> findByDoctor_DoctorEmail(String doctorEmail);

    Optional<Appointment> findFirstByPatient_EmailOrderByTimestampDesc(String email);

    Optional<Appointment> findFirstByPatient_EmailOrderByDateDesc(String email);

    List<Appointment> findByPatient_Email(@NonNull String email);

    List<Appointment> findByDate(LocalDate date);

    Integer countByDate(LocalDate date);

    List<Appointment> findAllByPatient(Patient patient);

    List<Appointment> findAllByPatientAndPrescriptionNotNull(Patient patient);

    List<Appointment> findAllByAppointmentIdInAndLocation(List<UUID> ids,Location location);

    Integer countByDateAndPrescriptionNotNull(LocalDate date);

    Integer countByPrescriptionNotNullAndDateNot(LocalDate date);

    List<Appointment> findAllByDateAndLocation(LocalDate localDate, Location location);

}
