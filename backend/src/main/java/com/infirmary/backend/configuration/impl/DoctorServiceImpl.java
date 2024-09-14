package com.infirmary.backend.configuration.impl;

import com.infirmary.backend.configuration.Exception.AppointmentNotFoundException;
import com.infirmary.backend.configuration.Exception.DoctorNotFoundException;
import com.infirmary.backend.configuration.dto.DoctorDTO;
import com.infirmary.backend.configuration.model.Appointment;
import com.infirmary.backend.configuration.model.Doctor;
import com.infirmary.backend.configuration.model.DoctorStatus;
import com.infirmary.backend.configuration.model.Prescription;
import com.infirmary.backend.configuration.repository.AppointmentRepository;
import com.infirmary.backend.configuration.repository.DoctorRepository;
import com.infirmary.backend.configuration.repository.DoctorStatusRepository;
import com.infirmary.backend.configuration.service.DoctorService;
import com.infirmary.backend.shared.utility.AppointmentQueueManager;
import com.infirmary.backend.shared.utility.MessageConfigUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Transactional
@Service
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorStatusRepository doctorStatusRepository;
    private final AppointmentRepository appointmentRepository;
    private final MessageConfigUtil messageConfigUtil;

    public DoctorServiceImpl(DoctorRepository doctorRepository, DoctorStatusRepository doctorStatusRepository, AppointmentRepository appointmentRepository, MessageConfigUtil messageConfigUtil) {
        this.doctorRepository = doctorRepository;
        this.doctorStatusRepository = doctorStatusRepository;
        this.appointmentRepository = appointmentRepository;
        this.messageConfigUtil = messageConfigUtil;
    }
    @Override
    public DoctorDTO getDoctorById(String id) throws DoctorNotFoundException {
        Optional<Doctor> doctor = doctorRepository.findByDoctorEmail(id);
        if (doctor.isEmpty()) {
            throw new DoctorNotFoundException(messageConfigUtil.getDoctorNotFoundException());
        }
        return new DoctorDTO(doctor.get());
    }
    @Override
    public DoctorStatus getDoctorStatusById(String id) throws DoctorNotFoundException {
        if (Objects.isNull(id)) {
            throw new DoctorNotFoundException(messageConfigUtil.getDoctorNotFoundException());
        }
        DoctorStatus status = doctorStatusRepository.findByDoctor_DoctorEmail(id);
        if (status == null) {
            throw new IllegalArgumentException("Doctor Status Not Found for Id:" + id);
        }
        return status;
    }
    @Override
    public DoctorStatus setDoctorStatus(String id, Boolean isDoctorCheckIn) throws DoctorNotFoundException {
        if (Objects.isNull(id)) {
            throw new DoctorNotFoundException(messageConfigUtil.getDoctorNotFoundException());
        }
        DoctorStatus doctorStatus = doctorStatusRepository.findByDoctor_DoctorEmail(id);
        if (doctorStatus == null) {
            throw new IllegalArgumentException("Doctor Status Not Found for the Id:" + id);
        }
        doctorStatus.setIsDoctorCheckIn(isDoctorCheckIn);
        return doctorStatusRepository.save(doctorStatus);
    }
    @Override
    public HashMap<String, Long> getAppointmentCountByDate(LocalDate date) throws AppointmentNotFoundException {
        if (Objects.isNull(date)) {
            throw new IllegalArgumentException("Date not found");
        }
        HashMap<String, Long> dayMetrics = new HashMap<>();
        List<Appointment> byDate = appointmentRepository.findByDate(date);
        if (byDate.isEmpty()) {
            throw new AppointmentNotFoundException(messageConfigUtil.getAppointmentNotFoundException());
        }
        dayMetrics.put("Total Appointment", (long) byDate.size());
        dayMetrics.put("In Queue", AppointmentQueueManager.getQueueSize());
        dayMetrics.put("Patients left", (long) byDate.size() - AppointmentQueueManager.getQueueSize());

        return dayMetrics;
    }
    @Override
    public HashMap<LocalDate, Prescription> getPrescriptionHistory(String email)
    {
        //put check
        List<Appointment> listOfAppointments = appointmentRepository.findByPatient_Email(email);
        HashMap<LocalDate, Prescription> mapOfPrescription = new HashMap<>();

        for (Appointment listOfAppointment : listOfAppointments) {
            mapOfPrescription.put(listOfAppointment.getDate(), listOfAppointment.getPrescription());
        }
        return mapOfPrescription;
    }
    @Override
    public List<DoctorDTO> getAvailableDoctors() throws DoctorNotFoundException {
        List<Doctor> byStatus = doctorRepository.findByStatusTrue();
        List<DoctorDTO> dtoList = byStatus.stream().map(DoctorDTO::new).toList();
        if (dtoList.isEmpty()) {
            throw new DoctorNotFoundException(messageConfigUtil.getDoctorNotFoundException());
        }
        return dtoList;
    }
    @Override
     public List<DoctorDTO> getAllDoctors() throws DoctorNotFoundException {
         List<Doctor> list = doctorRepository.findAll();
         if (list.isEmpty()) {
             throw new DoctorNotFoundException(messageConfigUtil.getDoctorNotFoundException());
         }
         return list.stream().map(DoctorDTO::new).toList();
     }
}
