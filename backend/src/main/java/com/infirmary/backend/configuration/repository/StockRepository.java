package com.infirmary.backend.configuration.repository;

import com.infirmary.backend.configuration.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    List<Stock> findByMedicineTypeIn(List<String> medicineTypes);

    List<Stock> findByQuantityGreaterThanEqualOrderByQuantityDesc(Long quantity);

    List<Stock> findByExpirationDateBefore(LocalDate expirationDate);

    List<Stock> findByCompanyIn(List<String> companies);

    Optional<Stock> findByBatchNumber(Long batchNumber);

    List<Stock> findByQuantityNull();

    List<Stock> findByExpirationDateAfter(LocalDate expirationDate);
}