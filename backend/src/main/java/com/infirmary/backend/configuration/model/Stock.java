package com.infirmary.backend.configuration.model;

import com.infirmary.backend.configuration.dto.StockDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "stocks")
public class Stock implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "batch_number")
    private Long batchNumber;

    @Column(name = "medicine_name")
    private String medicineName;

    @Column(name = "composition")
    private String composition;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "medicine_type")
    private String medicineType;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "company")
    private String company;

    public Stock(StockDTO stockDTO) {
        this.batchNumber = stockDTO.getBatchNumber();
        this.medicineName = stockDTO.getMedicineName();
        this.composition = stockDTO.getComposition();
        this.quantity = stockDTO.getQuantity();
        this.medicineType = stockDTO.getMedicineType();
        this.expirationDate = stockDTO.getExpirationDate();
        this.company = stockDTO.getCompany();
    }
}
