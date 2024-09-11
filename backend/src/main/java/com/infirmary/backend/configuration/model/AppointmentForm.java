package com.infirmary.backend.configuration.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "appointment_form")
public class AppointmentForm implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "is_follow_up", columnDefinition = "boolean default false")
    private Boolean isFollowUp;

    @Column(name = "pref_doctor")
    private String prefDoctor;

    @Column(name = "reason_for_pref")
    private String reasonForPreference;

    @OneToOne
    @JoinColumn(name = "appointment_id", referencedColumnName = "appointment_id", nullable = false)
    private Appointment appointment;
}
