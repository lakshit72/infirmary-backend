package com.infirmary.backend.configuration.dto;

import com.infirmary.backend.configuration.model.Stock;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class StockDTO {
    private UUID id;
    @NotNull(message = "Must assign a batch number")
    private Long batchNumber;

    @NotBlank(message = "Please provide medicine name")
    private String medicineName;

    @NotBlank(message = "Please provide composition")
    private String composition;

    @Min(message = "Please provide quantity", value = 1)
    @NotNull(message = "Quantity cannot be empty")
    private Long quantity;

    @NotBlank(message = "Please provide medicine type")
    private String medicineType;

    @Future(message = "Cannot add Expired medicine")
    @NotNull(message = "Expiration Date Cannot be empty")
    private LocalDate expirationDate;

    @NotBlank(message = "Please provide company name")
    private String company;

    public StockDTO(Stock stock) {
        this.id = stock.getId();
        this.batchNumber = stock.getBatchNumber();
        this.medicineName = stock.getMedicineName();
        this.composition = stock.getComposition();
        this.quantity = stock.getQuantity();
        this.medicineType = stock.getMedicineType();
        this.expirationDate = stock.getExpirationDate();
        this.company = stock.getCompany();
    }
}
