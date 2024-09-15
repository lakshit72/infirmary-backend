package com.infirmary.backend.configuration.impl;

import com.infirmary.backend.configuration.Exception.AppointmentNotFoundException;
import com.infirmary.backend.configuration.Exception.DoctorNotFoundException;
import com.infirmary.backend.configuration.Exception.PatientNotFoundException;
import com.infirmary.backend.configuration.dto.AppointmentDTO;
import com.infirmary.backend.configuration.model.Appointment;
import com.infirmary.backend.configuration.model.Prescription;
import com.infirmary.backend.configuration.repository.AppointmentRepository;
import com.infirmary.backend.configuration.service.AppointmentService;
import com.infirmary.backend.shared.utility.AppointmentQueueManager;
import com.infirmary.backend.shared.utility.MessageConfigUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Transactional
@Service
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final MessageConfigUtil messageConfigUtil;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, MessageConfigUtil messageConfigUtil) {
        this.appointmentRepository = appointmentRepository;
        this.messageConfigUtil = messageConfigUtil;
    }

    public AppointmentDTO getAppointmentById(Long appointmentId) throws AppointmentNotFoundException {
        Appointment appointment = appointmentRepository.findByAppointmentId(appointmentId);
        if (Objects.isNull(appointment)) {
            throw new AppointmentNotFoundException(messageConfigUtil.getAppointmentNotFoundException());
        }
        return new AppointmentDTO(appointment);
    }

    public List<AppointmentDTO> getAppointmentsByPatientId(String email) throws AppointmentNotFoundException,
            PatientNotFoundException {
        if (Objects.isNull(email)) {
            throw new PatientNotFoundException(messageConfigUtil.getPatientNotFound());
        } else {
            List<Appointment> appointmentList = appointmentRepository.findByPatient_Email(email);
            if (appointmentList.isEmpty()) {
                throw new AppointmentNotFoundException(messageConfigUtil.getAppointmentNotFoundException());
            }
            return appointmentList.stream().map(AppointmentDTO::new).toList();
        }
    }

    public List<AppointmentDTO> getAppointmentsByDoctorId(String doctorId) throws DoctorNotFoundException,
            AppointmentNotFoundException {
        if (Objects.isNull(doctorId)) {
            throw new PatientNotFoundException(messageConfigUtil.getDoctorNotFoundException());
        } else {
            List<Appointment> appointmentList = appointmentRepository.findByDoctor_DoctorEmail(doctorId);
            if (appointmentList.isEmpty()) {
                throw new AppointmentNotFoundException(messageConfigUtil.getAppointmentNotFoundException());
            }
            return appointmentList.stream().map(AppointmentDTO::new).toList();
        }
    }

    public LocalDate getLastAppointmentDateByEmail(String patientEmail) {
        Optional<Appointment> lastAppointment = appointmentRepository.findFirstByPatient_EmailOrderByDateDesc(patientEmail);
        return lastAppointment.map(Appointment::getDate).orElse(null);
    }


    public void scheduleAppointment(Long appointmentId) {
        AppointmentQueueManager.addAppointmentToQueue(appointmentId);
    }

    public Long getNextAppointment(){
        boolean res = AppointmentQueueManager.hasMoreAppointments();
        if (res) {
            return AppointmentQueueManager.getNextAppointment();
        }
        throw new RuntimeException("Queue empty!");
    }

    public AppointmentDTO getCurrentNextAppointment()throws AppointmentNotFoundException
    {
        Long nextId = getNextAppointment();
        // no need to check as getAppointmentById will be checking for null
        return getAppointmentById(nextId);
    }

    public List<Prescription> getPrescriptionUrlByPatientEmail(String email) throws PatientNotFoundException {
        if (Objects.isNull(email)) {
            throw new PatientNotFoundException(messageConfigUtil.getPatientNotFound());
        }
        List<Appointment> byEmail = appointmentRepository.findByPatient_Email(email);
        List<Prescription> list = byEmail.stream().map(Appointment::getPrescription).toList();
        if (list.isEmpty()) {
            throw new RuntimeException("No prescriptions urls can be found for the Id");
        }
        return list;
    }
}
