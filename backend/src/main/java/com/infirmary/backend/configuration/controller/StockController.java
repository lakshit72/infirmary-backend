package com.infirmary.backend.configuration.controller;

import com.infirmary.backend.configuration.Exception.StockAlreadyExists;
import com.infirmary.backend.configuration.Exception.StockNotFoundException;
import com.infirmary.backend.configuration.dto.StockDTO;
import com.infirmary.backend.configuration.service.StockService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.infirmary.backend.shared.utility.FunctionUtil.createSuccessResponse;

@RestController
@RequestMapping(value = "/api/stock")
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PreAuthorize("hasRole('ROLE_AD') or hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/byType")
    public ResponseEntity<?> getStockByType(@RequestParam List<String> type) throws StockNotFoundException {
        List<StockDTO> response = stockService.getStockByTypeIn(type);
        return createSuccessResponse(response);
    }

    @PreAuthorize("hasRole('ROLE_AD') or hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/before/ExpirationDate")
    public ResponseEntity<?> getStockBeforeExpirationDate(@RequestParam LocalDate expirationDate) throws
            StockNotFoundException {
        List<StockDTO> response = stockService.getStockBeforeExpirationDate(expirationDate);
        return createSuccessResponse(response);
    }

    @PreAuthorize("hasRole('ROLE_AD') or hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/after/ExpirationDate")
    public ResponseEntity<?> getStockAfterExpirationDate(@RequestParam LocalDate expirationDate) throws
            StockNotFoundException {
        List<StockDTO> response = stockService.getStockAfterExpirationDate(expirationDate);
        return createSuccessResponse(response);
    }

    @PreAuthorize("hasRole('ROLE_AD') or hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/byQuantity")
    public ResponseEntity<?> getStockByQuantityGreaterThan() throws
            StockNotFoundException {
        List<StockDTO> response = stockService.getStockByQuantityGreaterEqualThan();
        return createSuccessResponse(response);
    }

    @PreAuthorize("hasRole('ROLE_AD') or hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/companyNames")
    public ResponseEntity<?> getStockByCompanyNameIn(@RequestParam List<String> companyName) throws
            StockNotFoundException {
        List<StockDTO> response = stockService.getStocksByCompanyNameIn(companyName);
        return createSuccessResponse(response);
    }

    @PreAuthorize("hasRole('ROLE_AD') or hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/byBatchNumber/{batch-number}")
    public ResponseEntity<?> getStockByBatchNumber(@PathVariable("batch-number") Long batchNumber)
            throws StockNotFoundException {
        if (Objects.isNull(batchNumber)) {
            throw new IllegalArgumentException("Batch Number not found");
        }
        StockDTO response = stockService.getStockByBatchNumber(batchNumber);
        return createSuccessResponse(response);
    }

    @PreAuthorize("hasRole('ROLE_AD') or hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/getNullStock")
    public ResponseEntity<?> getNullStock() throws StockNotFoundException {
        List<StockDTO> response = stockService.getNullStock();
        return createSuccessResponse(response);
    }

    @PreAuthorize("hasRole('ROLE_AD') or hasRole('ROLE_DOCTOR')")
    @PostMapping(value = "/addStock")
    public ResponseEntity<?> addStock(@RequestBody StockDTO stockDTO, @RequestHeader(value = "X-Location",required = true) Long locId) throws StockAlreadyExists {
        StockDTO dto = stockService.addStock(stockDTO,locId);
        return createSuccessResponse(dto);
    }

    @PreAuthorize("hasRole('ROLE_AD') or hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/export")
    public ResponseEntity<?> exportStocks() throws IOException {
        byte[] excelContent = stockService.exportStocksToExcel();

        ByteArrayResource resource = new ByteArrayResource(excelContent);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=medicine_stocks.xlsx");
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentLength(excelContent.length);

        return createSuccessResponse(resource, headers);
    }

    @PreAuthorize("hasRole('ROLE_AD') or hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/")
    public ResponseEntity<?> getAllStock(){
        return ResponseEntity.ok(stockService.getAllStocks());
    }

}
