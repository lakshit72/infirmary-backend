package com.infirmary.backend.configuration.service;

import com.infirmary.backend.configuration.Exception.AppointmentNotFoundException;
import com.infirmary.backend.configuration.Exception.DoctorNotFoundException;
import com.infirmary.backend.configuration.dto.DoctorDTO;
import com.infirmary.backend.configuration.model.DoctorStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;

@Service
public interface DoctorService {
    DoctorDTO getDoctorById(Long id) throws DoctorNotFoundException;
    DoctorStatus getDoctorStatusById(Long id) throws DoctorNotFoundException;
    DoctorStatus setDoctorStatus(Long id, Boolean isDoctorCheckIn) throws DoctorNotFoundException;
    int getAppointmentCountByDate(LocalDate date) throws AppointmentNotFoundException;
    public HashMap<LocalDate, String> getPrescriptionHistory(String email);
}
