package com.infirmary.backend.configuration.model;

import com.infirmary.backend.configuration.dto.AppointmentDTO;
import com.infirmary.backend.configuration.dto.DoctorDTO;
import com.infirmary.backend.configuration.dto.PatientDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URL;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long appointmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sap_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "prescription_url")
    private URL prescriptionURL;

    public Appointment(AppointmentDTO appointmentDTO) {
        this.appointmentId = appointmentDTO.getAppointmentId();

        this.patient = convertToPatient(appointmentDTO.getPatient());
        this.doctor = convertToDoctor(appointmentDTO.getDoctor());

        this.date = appointmentDTO.getDate();
        this.prescriptionURL = appointmentDTO.getPrescriptionURL();
    }

    // convert PatientDTO to Patient entity
    private Patient convertToPatient(PatientDTO patientDTO) {
        return new Patient(patientDTO);
    }

    // convert DoctorDTO to Doctor entity
    private Doctor convertToDoctor(DoctorDTO doctorDTO) {
        Doctor doctor = new Doctor();
        doctor.setDoctorId(doctorDTO.getDoctorId());
        doctor.setName(doctorDTO.getName());
        doctor.setGender(doctorDTO.getGender());
        return doctor;
    }
}
