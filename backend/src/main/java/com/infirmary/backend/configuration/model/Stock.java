package com.infirmary.backend.configuration.model;

import com.infirmary.backend.configuration.dto.StockDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "stocks")
public class Stock implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "stock_id")
    private UUID Id;

    @Column(name = "batch_number")
    private Long batchNumber;

    @Column(name = "medicine_name",nullable = false)
    private String medicineName;

    @Column(name = "composition",nullable = false)
    private String composition;

    @Column(name = "quantity",nullable = false)
    private Long quantity;

    @Column(name = "medicine_type",nullable = false)
    private String medicineType;

    @Column(name = "expiration_date",nullable = false)
    private LocalDate expirationDate;

    @Column(name = "company",nullable = false)
    private String company;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location")
    private Location location;

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
