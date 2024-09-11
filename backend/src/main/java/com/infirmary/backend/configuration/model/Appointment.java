package com.infirmary.backend.configuration.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "appointment")
@NoArgsConstructor
public class Appointment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long appointmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sap_email", referencedColumnName = "sap_email", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", referencedColumnName = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "prescription_url")
    private String prescriptionURL;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "is_follow_up", columnDefinition = "boolean default false")
    private Boolean isFollowUp;

    @Column(name = "pref_doctor")
    private String prefDoctor;

    @Column(name = "reason_for_pref")
    private String reasonForPreference;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "prev_appointment",referencedColumnName = "appointment_id",nullable = true)
    private Appointment prevAppointment;


}
