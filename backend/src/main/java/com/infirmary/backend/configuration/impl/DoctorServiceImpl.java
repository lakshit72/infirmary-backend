package com.infirmary.backend.configuration.impl;

import com.infirmary.backend.configuration.Exception.DoctorNotFoundException;
import com.infirmary.backend.configuration.dto.DoctorDTO;
import com.infirmary.backend.configuration.model.Doctor;
import com.infirmary.backend.configuration.repository.DoctorRepository;
import com.infirmary.backend.configuration.service.DoctorService;
import com.infirmary.backend.shared.utility.MessageConfigUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@Transactional
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final MessageConfigUtil messageConfigUtil;

    public DoctorServiceImpl(DoctorRepository doctorRepository, MessageConfigUtil messageConfigUtil) {
        this.doctorRepository = doctorRepository;
        this.messageConfigUtil = messageConfigUtil;
    }

    public DoctorDTO getDoctorById(Long id) throws DoctorNotFoundException {
        try {
            Doctor doctor = doctorRepository.findByDoctorId(id);
            if (Objects.isNull(doctor)) {
                throw new DoctorNotFoundException(messageConfigUtil.getDoctorNotFoundException());
            }
            return new DoctorDTO(doctor);
        } catch (Exception e) {
            log.error("Exception in getDoctorById", e);
            throw e;
        }
    }
}
