package com.infirmary.backend.configuration.dto;

import java.time.LocalDate;

import com.infirmary.backend.configuration.model.Prescription;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatientDetails {
    private PatientDTO patient;
    private MedicalDetailsDTO medicalDetails;
    private PrescriptionInfo prescription;
    private String reason;
    private String docName;
    private String time;
    private LocalDate date;
    private Float temp;
    private String designation;


    public PatientDetails(PatientDTO patient,MedicalDetailsDTO medicalDetails,Prescription prescription){
        this.patient = patient;
        this.medicalDetails = medicalDetails;
        this.prescription = new PrescriptionInfo(prescription);
    }
}
