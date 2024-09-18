package com.infirmary.backend.configuration.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PrescriptionMeds implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medicine_id")
    private Long medId;

    @ManyToOne
    @JoinColumn(name = "medicine", referencedColumnName = "batch_number")
    private Stock medicine;
    
    @Column(name = "dosage")
    private Long dosage;

    @Column(name = "duration")
    private Long duration;

    @Column(name = "suggestion")
    private String suggestion; 
}
