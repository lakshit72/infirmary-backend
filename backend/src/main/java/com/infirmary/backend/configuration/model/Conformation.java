package com.infirmary.backend.configuration.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "conformation")
public class Conformation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "conformation_token")
    private UUID conformationToken;

    @OneToOne(fetch = FetchType.EAGER)
    private Patient patient;

    @OneToOne(fetch = FetchType.EAGER)
    private Doctor doctor;

    @OneToOne(fetch = FetchType.EAGER)
    private AD ad;
    
    @Column(name = "timestamp")
    private Long timestamp;
}
