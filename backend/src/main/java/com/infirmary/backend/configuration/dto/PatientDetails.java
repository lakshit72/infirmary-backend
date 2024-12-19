package com.infirmary.backend.configuration.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatientDetails {
    private PatientDTO patient;
    private MedicalDetailsDTO medicalDetails;
    private List<PrescriptionDTO> prescriptions;
    private String reason;
    private String docName;
    private String time;
    private LocalDate date;
    private Float temp;
    private String designation;


    public PatientDetails(PatientDTO patient,MedicalDetailsDTO medicalDetails,List<PrescriptionDTO> prescriptions){
        this.patient = patient;
        this.medicalDetails = medicalDetails;
        this.prescriptions = prescriptions;
    }
}
