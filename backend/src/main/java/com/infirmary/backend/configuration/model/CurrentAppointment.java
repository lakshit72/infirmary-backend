package com.infirmary.backend.configuration.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.security.SecureRandomParameters;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "current_appointment")
public class CurrentAppointment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "current_appointment_id")
    private Long currentAppointmentId;

    @ManyToOne
    @JoinColumn(name = "appointment_id", referencedColumnName = "appointment_id")
    private Appointment appointment;

    @OneToOne
    @JoinColumn(name = "sap_email", referencedColumnName = "sap_email")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "doctor_id")
    private Doctor doctor;
}
