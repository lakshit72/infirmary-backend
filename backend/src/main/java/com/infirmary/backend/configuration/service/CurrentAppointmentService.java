package com.infirmary.backend.configuration.service;

import com.infirmary.backend.configuration.Exception.CurrentAppointmentNotFoundException;
import com.infirmary.backend.configuration.dto.CurrentAppointmentDTO;

public interface CurrentAppointmentService {

    CurrentAppointmentDTO getCurrentAppointmentById(Long currentAppointmentId) throws CurrentAppointmentNotFoundException;

    CurrentAppointmentDTO createCurrentAppointment(CurrentAppointmentDTO currentAppointmentDTO);

    void validateCurrentAppointmentData(CurrentAppointmentDTO currentAppointmentDTO);
}
