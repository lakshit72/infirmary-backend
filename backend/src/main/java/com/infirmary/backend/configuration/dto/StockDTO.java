package com.infirmary.backend.configuration.dto;

import com.infirmary.backend.configuration.model.Stock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class StockDTO {
    private Long batchNumber;
    private String medicineName;
    private String composition;
    private Long quantity;
    private String medicineType;
    private LocalDate expirationDate;
    private String company;

    public StockDTO(Stock stock) {
        this.batchNumber = stock.getBatchNumber();
        this.medicineName = stock.getMedicineName();
        this.composition = stock.getComposition();
        this.quantity = stock.getQuantity();
        this.medicineType = stock.getMedicineType();
        this.expirationDate = stock.getExpirationDate();
        this.company = stock.getCompany();
    }
}
