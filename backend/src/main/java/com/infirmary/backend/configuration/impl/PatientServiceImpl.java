package com.infirmary.backend.configuration.impl;

import com.infirmary.backend.configuration.Exception.PatientNotFoundException;
import com.infirmary.backend.configuration.dto.PatientDTO;
import com.infirmary.backend.configuration.model.Patient;
import com.infirmary.backend.configuration.repository.PatientRepository;
import com.infirmary.backend.configuration.service.PatientService;
import com.infirmary.backend.shared.utility.MessageConfigUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Transactional
@Service
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final MessageConfigUtil messageConfigUtil;

    public PatientServiceImpl(PatientRepository patientRepository, MessageConfigUtil messageConfigUtil) {
        this.patientRepository = patientRepository;
        this.messageConfigUtil = messageConfigUtil;
    }

    public PatientDTO getPatientBySapId(Long sapId){
        try {
            Optional<Patient> patient = patientRepository.findBySapId(sapId);
            if (patient.isEmpty()){
                throw new PatientNotFoundException(messageConfigUtil.getPatientNotFound());
            }
            return new PatientDTO(patient.get());
        } catch (PatientNotFoundException e){
            log.error("PatientNotFound Exception in method getPatientBySapId", e);
            throw e;
        } catch (Exception e){
            log.error("Exception in getPatientBySapId", e);
            throw new RuntimeException(e);
        }
    }
}
