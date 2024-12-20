package com.infirmary.backend.configuration.model;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "prescription_medicine")
public class PrescriptionMeds implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "pres_medicine_id")
    private UUID presMedicineId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medicine",referencedColumnName = "stock_id")
    private Stock medicine;

    @Column(name = "dosage_morning")
    private Float dosageMorning;

    @Column(name = "dosage_afternoon")
    private Float dosageAfternoon;

    @Column(name = "dosage_evening")
    private Float dosageEvening;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "suggestion")
    private String suggestion; 

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="prescription")
    private Prescription prescription;
}
