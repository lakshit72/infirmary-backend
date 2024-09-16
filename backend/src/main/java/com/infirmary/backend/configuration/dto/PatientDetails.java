package com.infirmary.backend.configuration.dto;

import java.util.List;

import com.infirmary.backend.configuration.model.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatientDetails {
    private Patient patient;
    private MedicalDetails medicalDetails;
    private List<Prescription> prescriptions;

    public PatientDetails(Patient patient,MedicalDetails medicalDetails,List<Prescription> prescriptions){
        this.patient = patient;
        this.medicalDetails = medicalDetails;
        this.prescriptions = prescriptions;
    }
}
