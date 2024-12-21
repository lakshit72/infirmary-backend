package com.infirmary.backend.configuration.service;

import com.infirmary.backend.configuration.Exception.CurrentAppointmentNotFoundException;
import com.infirmary.backend.configuration.Exception.DoctorNotFoundException;
import com.infirmary.backend.configuration.dto.AppointmentResDTO;
import com.infirmary.backend.configuration.dto.CurrentAppointmentDTO;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public interface CurrentAppointmentService {
    CurrentAppointmentDTO getCurrentAppointmentById(UUID currentAppointmentId);
    AppointmentResDTO getAppointmentStatusDoctorStatus(UUID currentAppointmentId);
    CurrentAppointmentDTO getCurrAppByDoctorId(String docEmail) throws CurrentAppointmentNotFoundException,DoctorNotFoundException;
    
}
