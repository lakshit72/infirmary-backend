package com.infirmary.backend.configuration.model;

import java.io.Serializable;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pres_medicine_id")
    private Long presMedicineId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medicine",referencedColumnName = "batch_number")
    private Stock medicine;

    @Column(name = "dosage")
    private int dosage;

    @Column(name = "duration")
    private int duration;

    @Column(name = "suggestion")
    private String suggestion; 
}
