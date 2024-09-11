package com.infirmary.backend.configuration.service;

import com.infirmary.backend.configuration.Exception.AppointmentNotFoundException;
import com.infirmary.backend.configuration.Exception.DoctorNotFoundException;
import com.infirmary.backend.configuration.Exception.PatientNotFoundException;
import com.infirmary.backend.configuration.dto.AppointmentDTO;
import com.infirmary.backend.configuration.dto.AppointmentResDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface AppointmentService {
    AppointmentDTO getAppointmentById(Long appointmentId) throws AppointmentNotFoundException;

    List<AppointmentDTO> getAppointmentsByPatientId(String email) throws AppointmentNotFoundException,
            PatientNotFoundException;

    List<AppointmentDTO> getAppointmentsByDoctorId(Long doctorId) throws DoctorNotFoundException,
            AppointmentNotFoundException;

    AppointmentDTO getAppointmentByPrescriptionUrl(String url) throws AppointmentNotFoundException;

    LocalDate getLastAppointmentDateByEmail(String patientEmail);

    void scheduleAppointment(Long appointmentId);
    Long getNextAppointment();
    public AppointmentDTO getCurrentNextAppointment();
    List<String> getPrescriptionUrlByPatientEmail(String email) throws PatientNotFoundException;
}
