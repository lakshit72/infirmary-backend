package com.infirmary.backend.configuration.model;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "ad_prescription")
public class ADPrescription {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    private AD ad;

    @ManyToOne(fetch = FetchType.EAGER)
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    private Stock medicine;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "timestamp")
    private Long timestamp;

}
