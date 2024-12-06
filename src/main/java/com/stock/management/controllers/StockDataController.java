package com.stock.management.controllers;

import com.stock.management.template.APIBasedStockData;
import com.stock.management.template.FileStockDataProcessor;
import com.stock.management.template.StockDataTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stocks")
public class StockDataController {

    @PostMapping("/process/api")
    public ResponseEntity<String> processApiStockData() {
        try {
            StockDataTemplate processor = new APIBasedStockData();
            processor.fetchData();
            return ResponseEntity.ok("Stock data processed successfully from API.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process stock data from API: " + e.getMessage());
        }
    }

    @PostMapping("/process/file")
    public ResponseEntity<String> processFileStockData() {
        try {
            StockDataTemplate processor = new FileStockDataProcessor();
            processor.fetchData();
            return ResponseEntity.ok("Stock data processed successfully from File.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process stock data from File: " + e.getMessage());
        }
    }

//    @PostMapping("/process/database")
//    public ResponseEntity<String> processDatabaseStockData() {
//        try {
//            StockDataTemplate processor = new DatabaseStockDataProcessor();
//            processor.processStockData();
//            return ResponseEntity.ok("Stock data processed successfully from Database.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Failed to process stock data from Database: " + e.getMessage());
//        }
//    }


}
