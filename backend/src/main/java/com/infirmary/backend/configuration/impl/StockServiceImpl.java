package com.infirmary.backend.configuration.impl;

import com.infirmary.backend.configuration.Exception.StockAlreadyExists;
import com.infirmary.backend.configuration.Exception.StockNotFoundException;
import com.infirmary.backend.configuration.dto.StockDTO;
import com.infirmary.backend.configuration.model.Stock;
import com.infirmary.backend.configuration.repository.StockRepository;
import com.infirmary.backend.configuration.service.StockService;
import com.infirmary.backend.shared.utility.MessageConfigUtil;
import com.infirmary.backend.shared.utility.StockThreshold;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
    public List<StockDTO> getStockByQuantityGreaterEqualThan() throws StockNotFoundException {
        Long quantity = StockThreshold.DEFAULT_THRESHOLD.getL();
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

    public byte[] exportStocksToExcel() throws IOException {
        List<Stock> stocks = stockRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Medicine Stocks");

            Row headerRow = sheet.createRow(0);
            String[] columns = {"Batch Number", "Medicine Name", "Composition", "Quantity", "Medicine Type", "Expiration Date", "Company"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            int rowNum = 1;
            for (Stock stock : stocks) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(stock.getBatchNumber());
                row.createCell(1).setCellValue(stock.getMedicineName());
                row.createCell(2).setCellValue(stock.getComposition());
                row.createCell(3).setCellValue(stock.getQuantity());
                row.createCell(4).setCellValue(stock.getMedicineType());
                row.createCell(5).setCellValue(stock.getExpirationDate().toString());
                row.createCell(6).setCellValue(stock.getCompany());
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    @Override
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    
    @Override
    public void deleteStock(Long batchNumber) throws StockNotFoundException {
        Optional<Stock> batch = stockRepository.findByBatchNumber(batchNumber);
        if (batch.isEmpty()) {
            throw new StockNotFoundException(messageConfigUtil.getStockNotFound());
        }
        stockRepository.delete(batch.get());
    }

    @Override
    public List<Stock> getAvailableStock() {
        return stockRepository.findByQuantityGreaterThan(0);
    }
}
