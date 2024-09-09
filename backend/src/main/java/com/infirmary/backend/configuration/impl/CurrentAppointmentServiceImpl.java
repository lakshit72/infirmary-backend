package com.infirmary.backend.configuration.impl;

import com.infirmary.backend.configuration.Exception.CurrentAppointmentNotFoundException;
import com.infirmary.backend.configuration.dto.CurrentAppointmentDTO;
import com.infirmary.backend.configuration.model.CurrentAppointment;
import com.infirmary.backend.configuration.repository.CurrentAppointmentRepository;
import com.infirmary.backend.configuration.service.CurrentAppointmentService;
import com.infirmary.backend.shared.utility.MessageConfigUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@Transactional
public class CurrentAppointmentServiceImpl implements CurrentAppointmentService {
    private final CurrentAppointmentRepository currentAppointmentRepository;
    private final MessageConfigUtil messageConfigUtil;

    public CurrentAppointmentServiceImpl(CurrentAppointmentRepository currentAppointmentRepository, MessageConfigUtil messageConfigUtil) {
        this.currentAppointmentRepository = currentAppointmentRepository;
        this.messageConfigUtil = messageConfigUtil;
    }

    public CurrentAppointmentDTO getCurrentAppointmentById(Long currentAppointmentId) {
        CurrentAppointment appointment = currentAppointmentRepository.findByAppointment_AppointmentId(currentAppointmentId);
        if (Objects.isNull(appointment)) {
            throw new CurrentAppointmentNotFoundException(messageConfigUtil.getCurrentAppointmentNotFound());
        }
        return new CurrentAppointmentDTO(appointment);
    }


}
