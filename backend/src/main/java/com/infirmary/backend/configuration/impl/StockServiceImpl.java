package com.infirmary.backend.configuration.impl;

import com.infirmary.backend.configuration.Exception.StockAlreadyExists;
import com.infirmary.backend.configuration.Exception.StockNotFoundException;
import com.infirmary.backend.configuration.dto.StockDTO;
import com.infirmary.backend.configuration.model.Stock;
import com.infirmary.backend.configuration.repository.StockRepository;
import com.infirmary.backend.configuration.service.StockService;
import com.infirmary.backend.shared.utility.MessageConfigUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;
    private final MessageConfigUtil messageConfigUtil;

    public StockServiceImpl(StockRepository stockRepository, MessageConfigUtil messageConfigUtil) {
        this.stockRepository = stockRepository;
        this.messageConfigUtil = messageConfigUtil;
    }

    @Override
    public List<StockDTO> getStockByTypeIn(List<String> type) throws StockNotFoundException {
        if (type.isEmpty()) {
            throw new IllegalArgumentException("Type not found");
        }
        List<Stock> byMedicineTypeIn = stockRepository.findByMedicineTypeIn(type);
        List<StockDTO> list = byMedicineTypeIn.stream().map(StockDTO::new).toList();
        if (list.isEmpty()) {
            throw new StockNotFoundException(messageConfigUtil.getStockNotFound());
        }
        return list;
    }

    @Override
    public List<StockDTO> getStockBeforeExpirationDate(LocalDate expirationDate) throws StockNotFoundException {
        List<Stock> byExpirationDate = stockRepository.findByExpirationDateBefore(expirationDate);
        List<StockDTO> list = byExpirationDate.stream().map(StockDTO::new).toList();
        if (list.isEmpty()) {
            throw new StockNotFoundException(messageConfigUtil.getStockNotFound());
        }
        return list;
    }

    @Override
    public List<StockDTO> getStockAfterExpirationDate(LocalDate expirationDate) throws StockNotFoundException {
        List<Stock> byExpirationDate = stockRepository.findByExpirationDateAfter(expirationDate);
        List<StockDTO> list = byExpirationDate.stream().map(StockDTO::new).toList();
        if (list.isEmpty()) {
            throw new StockNotFoundException(messageConfigUtil.getStockNotFound());
        }
        return list;
    }

    @Override
    public List<StockDTO> getStockByQuantityGreaterEqualThan(Long quantity) throws StockNotFoundException {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity should be greater than 1");
        }
        List<Stock> list = stockRepository.findByQuantityGreaterThanEqualOrderByQuantityDesc(quantity);
        List<StockDTO> listDTO = list.stream().map(StockDTO::new).toList();
        if (listDTO.isEmpty()) {
            throw new StockNotFoundException(messageConfigUtil.getStockNotFound());
        }
        return listDTO;
    }

    @Override
    public List<StockDTO> getStocksByCompanyNameIn(List<String> companyNames) throws StockNotFoundException {
        if (companyNames.isEmpty()) {
            throw new IllegalArgumentException("Company names not found");
        }
        List<Stock> byCompany = stockRepository.findByCompanyIn(companyNames);
        List<StockDTO> list = byCompany.stream().map(StockDTO::new).toList();
        if (list.isEmpty()) {
            throw new StockNotFoundException(messageConfigUtil.getStockNotFound());
        }
        return list;
    }
    @Override
    public StockDTO getStockByBatchNumber(Long batchNumber) throws StockNotFoundException {
        if (Objects.isNull(batchNumber)) {
            throw new IllegalArgumentException("Batch Number not found");
        }
        Optional<Stock> byBatchNumber = stockRepository.findByBatchNumber(batchNumber);
        if (byBatchNumber.isEmpty()) {
            throw new StockNotFoundException(messageConfigUtil.getStockNotFound());
        }
        return new StockDTO(byBatchNumber.get());
    }
    @Override
    public List<StockDTO> getNullStock() throws StockNotFoundException {
        List<Stock> list = stockRepository.findByQuantityNull();
        List<StockDTO> dtoList = list.stream().map(StockDTO::new).toList();
        if (dtoList.isEmpty()) {
            throw new StockNotFoundException(messageConfigUtil.getStockNotFound());
        }
        return dtoList;
    }
    @Override
    public StockDTO addStock(StockDTO stockDTO) throws StockAlreadyExists {
        Optional<Stock> byBatch = stockRepository.findByBatchNumber(stockDTO.getBatchNumber());
        if (byBatch.isPresent()) {
            throw new StockAlreadyExists(messageConfigUtil.getStockAlreadyExists());
        }
        Stock stock = new Stock(stockDTO);
        Stock savedStock = stockRepository.save(stock);
        return new StockDTO(savedStock);
    }
}
