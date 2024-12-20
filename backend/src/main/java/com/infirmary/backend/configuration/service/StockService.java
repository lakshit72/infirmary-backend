package com.infirmary.backend.configuration.service;

import com.infirmary.backend.configuration.Exception.StockAlreadyExists;
import com.infirmary.backend.configuration.Exception.StockNotFoundException;
import com.infirmary.backend.configuration.dto.StockDTO;
import com.infirmary.backend.configuration.model.Stock;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
@Service
public interface StockService {
    List<StockDTO> getStockByTypeIn(List<String> type) throws StockNotFoundException;
    List<StockDTO> getStockBeforeExpirationDate(LocalDate expirationDate) throws StockNotFoundException;
    List<StockDTO> getStockAfterExpirationDate(LocalDate expirationDate) throws StockNotFoundException;
    List<StockDTO> getStockByQuantityGreaterEqualThan() throws StockNotFoundException;
    List<StockDTO> getStocksByCompanyNameIn(List<String> companyNames) throws StockNotFoundException;
    StockDTO getStockByBatchNumber(Long batchNumber) throws StockNotFoundException;
    List<StockDTO> getNullStock() throws StockNotFoundException;
    StockDTO addStock(StockDTO stockDTO,Long locId) throws StockAlreadyExists;
    byte[] exportStocksToExcel() throws IOException;
    List<Stock> getAllStocks();
    void deleteStock(UUID stockUuid) throws StockNotFoundException;
    List<Stock> getAvailableStock(Double longitude, Double latitude);
    String editStock(StockDTO stockDTO,Long locId);
}
