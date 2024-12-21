package com.infirmary.backend.configuration.impl;

import com.infirmary.backend.configuration.Exception.AppointmentNotFoundException;
import com.infirmary.backend.configuration.Exception.DoctorNotFoundException;
import com.infirmary.backend.configuration.Exception.PatientNotFoundException;
import com.infirmary.backend.configuration.Exception.PrescriptionNotFoundException;
import com.infirmary.backend.configuration.dto.AppointmentDTO;
import com.infirmary.backend.configuration.dto.PrescriptionDTO;
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
import java.util.UUID;

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

    @Override
    public AppointmentDTO getAppointmentById(UUID appointmentId) throws AppointmentNotFoundException {
        Appointment appointment = appointmentRepository.findByAppointmentId(appointmentId);
        if (Objects.isNull(appointment)) {
            throw new AppointmentNotFoundException(messageConfigUtil.getAppointmentNotFoundException());
        }
        return new AppointmentDTO(appointment);
    }

    @Override
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

    @Override
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

    @Override
    public LocalDate getLastAppointmentDateByEmail(String patientEmail) throws AppointmentNotFoundException {
        Optional<Appointment> lastAppointment = appointmentRepository.
                findFirstByPatient_EmailOrderByDateDesc(patientEmail);
        if (lastAppointment.isEmpty()) {
            throw new AppointmentNotFoundException(messageConfigUtil.getAppointmentNotFoundException());
        }
        return lastAppointment.map(Appointment::getDate).orElse(null);
    }

    @Override
    public void scheduleAppointment(UUID appointmentId) {
        AppointmentQueueManager.addAppointmentToQueue(appointmentId);
    }
    
    @Override
    public UUID getNextAppointment() {
        boolean res = AppointmentQueueManager.hasMoreAppointments();
        if (res) {
            return AppointmentQueueManager.getNextAppointment();
        }
        throw new RuntimeException("Queue empty!");
    }

    @Override
    public AppointmentDTO getCurrentNextAppointment() throws AppointmentNotFoundException {
        UUID nextId = getNextAppointment();
        // no need to check as getAppointmentById will be checking for null
        return getAppointmentById(nextId);
    }
    
    @Override
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
    
    @Override
    public PrescriptionDTO getPrescriptionByAppointmentId(UUID appointmentId) throws PatientNotFoundException ,
            PrescriptionNotFoundException {
        if (Objects.isNull(appointmentId)) {
            throw new AppointmentNotFoundException(messageConfigUtil.getAppointmentNotFoundException());
        }
        Appointment appointment = appointmentRepository.findByAppointmentId(appointmentId);
        Prescription prescription = appointment.getPrescription();
        if (Objects.isNull(prescription)) {
            throw new PrescriptionNotFoundException(messageConfigUtil.getPrescriptionNotFoundException());
        }
        return new PrescriptionDTO(prescription);
    }
}
