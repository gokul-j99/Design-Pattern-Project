package com.stock.management.controllers;

import com.stock.management.decorator.DetailedStockReport;
import com.stock.management.models.Stock;
import com.stock.management.models.UserStock;
import com.stock.management.storage.InMemoryDatabase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class StockReportController {

    @PostMapping("/detailed")
    public ResponseEntity<Object> getDetailedReport(@RequestParam String stockName,@RequestParam String username) {
        try {
            // Fetch stock from the database
            Stock stock = InMemoryDatabase.getStock(stockName);
            if (stock == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Stock not found: " + stockName);
            }
            UserStock userStock = new UserStock(username,stockName);
            // Decorate the stock with detailed report functionality
            Stock decoratedStock = new DetailedStockReport(userStock);
            String report = decoratedStock.display();

            // Return the detailed report
            return ResponseEntity.ok(report);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }
}
