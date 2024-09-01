package com.infirmary.backend.configuration.service;

import com.infirmary.backend.configuration.Exception.AppointmentNotFoundException;
import com.infirmary.backend.configuration.dto.AppointmentDTO;

public interface AppointmentService {
    AppointmentDTO getAppointmentById(Long appointmentId) throws AppointmentNotFoundException;
    AppointmentDTO createAppointment(AppointmentDTO appointmentDTO);
    void validateAppointmentData(AppointmentDTO appointmentDTO);
}
