package com.infirmary.backend.configuration.impl;

import com.infirmary.backend.configuration.Exception.AppointmentNotFoundException;
import com.infirmary.backend.configuration.Exception.DoctorNotFoundException;
import com.infirmary.backend.configuration.dto.DoctorDTO;
import com.infirmary.backend.configuration.model.Appointment;
import com.infirmary.backend.configuration.model.Doctor;
import com.infirmary.backend.configuration.model.DoctorStatus;
import com.infirmary.backend.configuration.repository.AppointmentRepository;
import com.infirmary.backend.configuration.repository.DoctorRepository;
import com.infirmary.backend.configuration.repository.DoctorStatusRepository;
import com.infirmary.backend.configuration.service.DoctorService;
import com.infirmary.backend.shared.utility.MessageConfigUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Slf4j
@Transactional
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

    public DoctorDTO getDoctorById(Long id) throws DoctorNotFoundException {
        Optional<Doctor> doctor = doctorRepository.findByDoctorId(id);
        if (doctor.isEmpty()) {
            throw new DoctorNotFoundException(messageConfigUtil.getDoctorNotFoundException());
        }
        return new DoctorDTO(doctor.get());
    }

    public DoctorStatus getDoctorStatusById(Long id) throws DoctorNotFoundException {
        if (Objects.isNull(id)) {
            throw new DoctorNotFoundException(messageConfigUtil.getDoctorNotFoundException());
        }
        DoctorStatus status = doctorStatusRepository.findByDoctor_DoctorId(id);
        if (status == null) {
            throw new IllegalArgumentException("Doctor Status Not Found for Id:" + id);
        }
        return status;
    }

    public DoctorStatus setDoctorStatus(Long id, Boolean isDoctorCheckIn) throws DoctorNotFoundException {
        if (Objects.isNull(id)) {
            throw new DoctorNotFoundException(messageConfigUtil.getDoctorNotFoundException());
        }
        DoctorStatus doctorStatus = doctorStatusRepository.findByDoctor_DoctorId(id);
        if (doctorStatus == null) {
            throw new IllegalArgumentException("Doctor Status Not Found for the Id:" + id);
        }
        doctorStatus.setIsDoctorCheckIn(isDoctorCheckIn);
        return doctorStatusRepository.save(doctorStatus);
    }

    public int getAppointmentCountByDate(LocalDate date) throws AppointmentNotFoundException {
        if (Objects.isNull(date)) {
            throw new IllegalArgumentException("Date not found");
        }
        List<Appointment> byDate = appointmentRepository.findByDate(date);
        if (byDate.isEmpty()) {
            throw new AppointmentNotFoundException(messageConfigUtil.getAppointmentNotFoundException());
        }
        return byDate.size();
    }

    public HashMap<LocalDate, String> getPrescriptionHistory(String email)
    {
        //put check
        List<Appointment> listOfAppointments = appointmentRepository.findByPatient_Email(email);
        HashMap<LocalDate, String> mapOfPrescription = new HashMap<>();

        for(int i=0; i<listOfAppointments.size(); ++i)
        {
            mapOfPrescription.put(listOfAppointments.get(i).getDate(), listOfAppointments.get(i).getPrescriptionURL());
        }
        return mapOfPrescription;
    }
}
