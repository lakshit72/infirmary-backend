package com.infirmary.backend.configuration.dto;

import com.infirmary.backend.configuration.model.Prescription;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PrescriptionDTO {
    private Long prescriptionId;
    private PatientDTO patientDTO;
    private DoctorDTO doctorDTO;
    private String diagnosis;
    private StockDTO stockDTO;
    private Long dosage;
    private Long duration;
    private String suggestion;
    private String dietaryRemarks;
    private String testNeeded;

    public PrescriptionDTO(Prescription prescription) {
        this.prescriptionId = prescription.getPrescriptionId();
        this.patientDTO = new PatientDTO(prescription.getPatient());
        this.doctorDTO = new DoctorDTO(prescription.getDoctor());
        this.diagnosis = prescription.getDiagnosis();
        this.stockDTO = new StockDTO(prescription.getStock());
        this.dosage = prescription.getDosage();
        this.duration = prescription.getDuration();
        this.suggestion = prescription.getSuggestion();
        this.dietaryRemarks = prescription.getDietaryRemarks();
        this.testNeeded = prescription.getTestNeeded();
    }
}

