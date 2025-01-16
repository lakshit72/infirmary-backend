package com.infirmary.backend.configuration.repository;

import com.infirmary.backend.configuration.model.Appointment;
import com.infirmary.backend.configuration.model.Location;
import com.infirmary.backend.configuration.model.Patient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    List<Appointment> findAllByDateAndLocationAndPrescriptionNotNull(LocalDate localDate, Location location);

    @Query("SELECT COUNT(DISTINCT a.patient) FROM Appointment a")
    Long countDistinctPatients();

    @Query(value = "SELECT p.school AS name, COUNT(a.appointment_id) AS count " +
               "FROM appointment a " +
               "JOIN patient p ON a.sap_email = p.sap_email " +
               "WHERE a.date >= :startDate " +
               "GROUP BY p.school", nativeQuery = true)
    List<Object[]> countAppointmentsGroupedBySchoolNative(@Param("startDate") LocalDate startDate);

    @Query(value = "SELECT m.residence_type AS name, COUNT(a.appointment_id) AS count " +
               "FROM appointment a " +
               "JOIN patient p ON p.sap_email = a.sap_email " +
               "JOIN medical_details m ON m.sap_email = p.sap_email "+
               "WHERE a.date >= :startDate " +
               "GROUP BY m.residence_type", nativeQuery = true)
    List<Object[]> countAppointmentsGroupedByResidenceType(@Param("startDate") LocalDate startDate);

    @Query(value = "SELECT d.doctor_id, COUNT(a.appointment_id) AS count " +
               "FROM appointment a " +
               "JOIN doctor d ON a.doctor_email = d.doctor_email " +
               "WHERE a.date >= :startDate " +
               "GROUP BY d.doctor_id", nativeQuery = true)
    List<Object[]> countAppointmentsGroupedByDoctor(@Param("startDate") LocalDate startDate);

    @Query(value = "SELECT TO_CHAR(a.date, 'YYYY-MM') AS month, l.location_name AS location, COUNT(a.appointment_id) AS patient_count FROM appointment a JOIN locations l ON a.location = l.location_id WHERE a.date >= CURRENT_DATE - INTERVAL '12 months' GROUP BY TO_CHAR(a.date, 'YYYY-MM'), l.location_name ORDER BY month ASC, location ASC",nativeQuery = true)
    List<Object[]> countAppointmentByMonth();

    @Query(value = "SELECT EXTRACT(YEAR FROM a.date) AS year, l.location_name AS location, COUNT(a.appointment_id) AS patient_count from appointment a JOIN locations l ON a.location = l.location_id WHERE a.date >= CURRENT_DATE - INTERVAL '5 years' GROUP BY EXTRACT(YEAR FROM a.date), l.location_name ORDER BY year ASC",nativeQuery = true)
    List<Object[]> countAppointmentByYear();

    @Query(value = "SELECT a.date, l.location_name, COUNT(a.appointment_id) " +
               "FROM appointment a " +
               "JOIN locations l ON a.location = l.location_id " +
               "WHERE a.date >= :startDate " +
               "GROUP BY a.date, l.location_name", nativeQuery = true)
    List<Object[]> countAppointmentsByDate(@Param("startDate") LocalDate startDate);

}
