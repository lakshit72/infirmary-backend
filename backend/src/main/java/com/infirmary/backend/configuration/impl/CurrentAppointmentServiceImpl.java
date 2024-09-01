package com.infirmary.backend.configuration.impl;

import com.infirmary.backend.configuration.Exception.CurrentAppointmentNotFoundException;
import com.infirmary.backend.configuration.dto.CurrentAppointmentDTO;
import com.infirmary.backend.configuration.model.Appointment;
import com.infirmary.backend.configuration.model.CurrentAppointment;
import com.infirmary.backend.configuration.model.Doctor;
import com.infirmary.backend.configuration.model.Patient;
import com.infirmary.backend.configuration.repository.AppointmentRepository;
import com.infirmary.backend.configuration.repository.CurrentAppointmentRepository;
import com.infirmary.backend.configuration.repository.DoctorRepository;
import com.infirmary.backend.configuration.repository.PatientRepository;
import com.infirmary.backend.configuration.service.CurrentAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CurrentAppointmentServiceImpl implements CurrentAppointmentService {

    private final CurrentAppointmentRepository currentAppointmentRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Autowired
    public CurrentAppointmentServiceImpl(CurrentAppointmentRepository currentAppointmentRepository,
                                         AppointmentRepository appointmentRepository,
                                         PatientRepository patientRepository,
                                         DoctorRepository doctorRepository) {
        this.currentAppointmentRepository = currentAppointmentRepository;
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public CurrentAppointmentDTO getCurrentAppointmentById(Long currentAppointmentId) throws CurrentAppointmentNotFoundException {
        Optional<CurrentAppointment> currentAppointment = currentAppointmentRepository.findByCurrentAppointmentId(currentAppointmentId);
        if (currentAppointment.isPresent()) {
            return new CurrentAppointmentDTO(currentAppointment.get());
        } else {
            throw new CurrentAppointmentNotFoundException("Current appointment not found with ID: " + currentAppointmentId);
        }
    }

    @Override
    public CurrentAppointmentDTO createCurrentAppointment(CurrentAppointmentDTO currentAppointmentDTO) {
        validateCurrentAppointmentData(currentAppointmentDTO);

        Appointment appointment = appointmentRepository.findById(currentAppointmentDTO.getAppointment().getAppointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid appointment ID"));
        Patient patient = patientRepository.findById(currentAppointmentDTO.getPatient().getSapId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid patient ID"));
        Doctor doctor = doctorRepository.findById(currentAppointmentDTO.getDoctor().getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid doctor ID"));

        CurrentAppointment currentAppointment = new CurrentAppointment(appointment, patient, doctor);
        CurrentAppointment savedCurrentAppointment = currentAppointmentRepository.save(currentAppointment);
        return new CurrentAppointmentDTO(savedCurrentAppointment);
    }

    @Override
    public void validateCurrentAppointmentData(CurrentAppointmentDTO currentAppointmentDTO) {
        if (currentAppointmentDTO.getAppointment() == null ||
                currentAppointmentDTO.getPatient() == null ||
                currentAppointmentDTO.getDoctor() == null) {
            throw new IllegalArgumentException("Appointment, Patient, and Doctor must not be null");
        }
    }
}
