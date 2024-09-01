package com.infirmary.backend.configuration.impl;

import com.infirmary.backend.configuration.Exception.AppointmentNotFoundException;
import com.infirmary.backend.configuration.dto.AppointmentDTO;
import com.infirmary.backend.configuration.model.Appointment;
import com.infirmary.backend.configuration.repository.AppointmentRepository;
import com.infirmary.backend.configuration.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public AppointmentDTO getAppointmentById(Long appointmentId) throws AppointmentNotFoundException {
        Optional<Appointment> appointment = appointmentRepository.findByAppointmentId(appointmentId);
        if (appointment.isPresent()) {
            return new AppointmentDTO(appointment.get());
        } else {
            throw new AppointmentNotFoundException("Appointment not found with ID: " + appointmentId);
        }
    }

    @Override
    public AppointmentDTO createAppointment(AppointmentDTO appointmentDTO) {
        validateAppointmentData(appointmentDTO);
        Appointment appointment = new Appointment(appointmentDTO);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return new AppointmentDTO(savedAppointment);
    }

    @Override
    public void validateAppointmentData(AppointmentDTO appointmentDTO) {
        if (appointmentDTO.getPatient() == null || appointmentDTO.getDoctor() == null) {
            throw new IllegalArgumentException("Patient and Doctor must not be null");
        }
        if (appointmentDTO.getDate() == null) {
            throw new IllegalArgumentException("Appointment date must not be null");
        }
    }
}
