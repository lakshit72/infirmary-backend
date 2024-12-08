package com.infirmary.backend.configuration.service;

import com.infirmary.backend.configuration.Exception.CurrentAppointmentNotFoundException;
import com.infirmary.backend.configuration.Exception.DoctorNotFoundException;
import com.infirmary.backend.configuration.dto.AppointmentResDTO;
import com.infirmary.backend.configuration.dto.CurrentAppointmentDTO;
import org.springframework.stereotype.Service;

@Service
public interface CurrentAppointmentService {
    CurrentAppointmentDTO getCurrentAppointmentById(Long currentAppointmentId);
    AppointmentResDTO getAppointmentStatusDoctorStatus(Long currentAppointmentId);
    CurrentAppointmentDTO getCurrAppByDoctorId(String docEmail) throws CurrentAppointmentNotFoundException,DoctorNotFoundException;
    
}
