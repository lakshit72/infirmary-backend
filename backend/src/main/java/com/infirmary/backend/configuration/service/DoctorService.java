package com.infirmary.backend.configuration.service;

import com.infirmary.backend.configuration.Exception.AppointmentNotFoundException;
import com.infirmary.backend.configuration.Exception.DoctorNotFoundException;
import com.infirmary.backend.configuration.dto.DoctorDTO;
import com.infirmary.backend.configuration.model.Doctor;
import com.infirmary.backend.configuration.model.Patient;
import com.infirmary.backend.configuration.model.Prescription;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Service
public interface DoctorService {
    DoctorDTO getDoctorById(String id) throws DoctorNotFoundException;
    Boolean getDoctorStatusById(String id) throws DoctorNotFoundException;
    Doctor setDoctorStatus(String id, Boolean isDoctorCheckIn) throws DoctorNotFoundException;
    HashMap<String, Long> getAppointmentCountByDate(LocalDate date) throws AppointmentNotFoundException;
    HashMap<LocalDate, Prescription> getPrescriptionHistory(String email);
    List<DoctorDTO> getAvailableDoctors() throws DoctorNotFoundException;
    List<DoctorDTO> getAllDoctors() throws DoctorNotFoundException;
    Patient getPatient(String doctorEmail);
}
