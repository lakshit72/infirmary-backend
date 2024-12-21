package com.infirmary.backend.configuration.model;

import com.infirmary.backend.configuration.dto.AppointmentDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "appointment")
@NoArgsConstructor
public class Appointment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "appointment_id")
    private UUID appointmentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sap_email", referencedColumnName = "sap_email", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_email", referencedColumnName = "doctor_email", nullable = true)
    private Doctor doctor;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "apt_form",referencedColumnName = "id")
    private AppointmentForm aptForm;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name =  "token_no")
    private Integer tokenNo;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "prescription_id", referencedColumnName = "prescription_id",nullable = true)
    private Prescription prescription;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location")
    private Location location;

    @Column(name = "weight")
    private Float weight;

    @Column(name = "temperature")
    private Float temperature;

    @Column(name = "timestamp")
    private long timestamp;

    public Appointment(AppointmentDTO appointmentDTO) {
        this.patient = new Patient(appointmentDTO.getPatientDTO());
        this.doctor = new Doctor(appointmentDTO.getDoctorDTO());
        this.date = appointmentDTO.getDate();
        this.prescription = new Prescription(appointmentDTO.getPrescriptionDTO());
        this.tokenNo = appointmentDTO.getTokenNo();
        this.temperature = appointmentDTO.getTemperature();
        this.weight = appointmentDTO.getWeight();
    }
}
