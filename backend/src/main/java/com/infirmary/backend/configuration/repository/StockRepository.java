package com.infirmary.backend.configuration.repository;

import com.infirmary.backend.configuration.model.Location;
import com.infirmary.backend.configuration.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockRepository extends JpaRepository<Stock, UUID> {

    List<Stock> findByMedicineTypeIn(List<String> medicineTypes);

    List<Stock> findByQuantityGreaterThanEqualOrderByQuantityDesc(Long quantity);

    List<Stock> findByExpirationDateBefore(LocalDate expirationDate);

    List<Stock> findByCompanyIn(List<String> companies);

    Optional<Stock> findByBatchNumber(Long batchNumber);

    List<Stock> findByQuantityNull();

    List<Stock> findByExpirationDateAfter(LocalDate expirationDate);
    
    List<Stock> findByQuantityGreaterThanAndLocationAndExpirationDateAfter(int quantity, Location location, LocalDate date);

    List<Stock> findByQuantityGreaterThan(int quantity);
}
