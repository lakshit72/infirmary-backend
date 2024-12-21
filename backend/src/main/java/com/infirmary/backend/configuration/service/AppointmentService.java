package com.infirmary.backend.configuration.service;

import com.infirmary.backend.configuration.Exception.AppointmentNotFoundException;
import com.infirmary.backend.configuration.Exception.DoctorNotFoundException;
import com.infirmary.backend.configuration.Exception.PatientNotFoundException;
import com.infirmary.backend.configuration.Exception.PrescriptionNotFoundException;
import com.infirmary.backend.configuration.dto.AppointmentDTO;
import com.infirmary.backend.configuration.dto.PrescriptionDTO;
import com.infirmary.backend.configuration.model.Prescription;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public interface AppointmentService {
    AppointmentDTO getAppointmentById(UUID appointmentId) throws AppointmentNotFoundException;

    List<AppointmentDTO> getAppointmentsByPatientId(String email) throws AppointmentNotFoundException,
            PatientNotFoundException;

    List<AppointmentDTO> getAppointmentsByDoctorId(String doctorId) throws DoctorNotFoundException,
            AppointmentNotFoundException;

    LocalDate getLastAppointmentDateByEmail(String patientEmail) throws AppointmentNotFoundException;

    void scheduleAppointment(UUID appointmentId);
    UUID getNextAppointment();
    AppointmentDTO getCurrentNextAppointment();
    List<Prescription> getPrescriptionUrlByPatientEmail(String email) throws PatientNotFoundException;
    PrescriptionDTO getPrescriptionByAppointmentId(UUID appointmentId) throws PatientNotFoundException ,
            PrescriptionNotFoundException;
}
