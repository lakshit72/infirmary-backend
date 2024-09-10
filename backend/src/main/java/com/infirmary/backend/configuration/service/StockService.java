package com.infirmary.backend.configuration.service;

import com.infirmary.backend.configuration.Exception.StockAlreadyExists;
import com.infirmary.backend.configuration.Exception.StockNotFoundException;
import com.infirmary.backend.configuration.dto.StockDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
public interface StockService {
    List<StockDTO> getStockByTypeIn(List<String> type) throws StockNotFoundException;
    List<StockDTO> getStockBeforeExpirationDate(LocalDate expirationDate) throws StockNotFoundException;
    List<StockDTO> getStockAfterExpirationDate(LocalDate expirationDate) throws StockNotFoundException;
    List<StockDTO> getStockByQuantityGreaterEqualThan(Long quantity) throws StockNotFoundException;
    List<StockDTO> getStocksByCompanyNameIn(List<String> companyNames) throws StockNotFoundException;
    StockDTO getStockByBatchNumber(Long batchNumber) throws StockNotFoundException;
    List<StockDTO> getNullStock() throws StockNotFoundException;
    StockDTO addStock(StockDTO stockDTO) throws StockAlreadyExists;
}