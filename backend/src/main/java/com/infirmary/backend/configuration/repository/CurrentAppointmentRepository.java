package com.infirmary.backend.configuration.repository;

import com.infirmary.backend.configuration.model.CurrentAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.List;

public interface CurrentAppointmentRepository extends JpaRepository<CurrentAppointment, Long> {

    Optional<CurrentAppointment> findByCurrentAppointmentId(@NonNull Long currentAppointmentId);

    List<CurrentAppointment> findByAppointment_AppointmentId(@NonNull Long appointmentId);

    List<CurrentAppointment> findByPatient_SapId(@NonNull Long sapId);

    List<CurrentAppointment> findByDoctor_DoctorId(@NonNull Long doctorId);
}
