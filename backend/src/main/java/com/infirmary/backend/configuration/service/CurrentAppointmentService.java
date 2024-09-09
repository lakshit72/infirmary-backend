package com.infirmary.backend.configuration.service;

import com.infirmary.backend.configuration.dto.CurrentAppointmentDTO;
import org.springframework.stereotype.Service;

@Service
public interface CurrentAppointmentService {
    CurrentAppointmentDTO getCurrentAppointmentById(Long currentAppointmentId);
}
