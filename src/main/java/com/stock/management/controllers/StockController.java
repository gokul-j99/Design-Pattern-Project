package com.stock.management.controllers;

import com.stock.management.factory.StockFactory;
import com.stock.management.models.Stock;
import com.stock.management.observer.StockNotifier;
import com.stock.management.storage.InMemoryDatabase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    @PostMapping("/add")
    public String addStock(
            @RequestParam String username,
            @RequestParam String stockName,
            @RequestParam double price,
            @RequestParam int quantity,
            @RequestParam String type) {
        String role = InMemoryDatabase.getUserRole(username);
        if (role == null || !role.equals("admin")) {
            return "Permission denied. Only admins can add stocks.";
        }
        Stock stock = StockFactory.getInstance().createStock(type,stockName,price,quantity);

        return "Stock '" + stock.getName() + "' added to the database.";
    }


    @PostMapping("/update")
    public ResponseEntity<String> updateStockPrice( @RequestParam String username,@RequestParam String stockName,
                                    @RequestParam(required = false) Double newPrice,
                                    @RequestParam(required = false) Integer quantity) {

        String role = InMemoryDatabase.getUserRole(username);
        if (role == null || !role.equals("admin")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Permission denied. Only admins can update stocks.");
        }
        if (newPrice == null && quantity == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Please provide either a new price or quantity to update.");
        }

        StringBuilder message = new StringBuilder();

        if (newPrice != null) {
            boolean priceUpdated = InMemoryDatabase.updateStockPrice(stockName, newPrice);
            if (!priceUpdated) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The stock '" + stockName + "' is not in the database. Please add the stock.");
            }
            message.append("Stock price updated to $").append(newPrice).append(". ");
        }

        if(quantity != null) {
            boolean quantityUpdated = InMemoryDatabase.updateStockQuantity(stockName, quantity);
            if (!quantityUpdated) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The stock '" + stockName + "' is not in the database. Please add the stock.");
            }
            message.append("Stock quantity updated to ").append(quantity).append(". ");
        }

        // Notify observers
        InMemoryDatabase.notifyStockUpdate(stockName, message.toString());

        return ResponseEntity.ok("Update successful for stock '" + stockName + "': " + message.toString());
    }

    @GetMapping("/list")
    public ResponseEntity<Object> listStocks(@RequestParam String username) {
        String role = InMemoryDatabase.getUserRole(username);

        if (role == null || !(role.equals("user") || role.equals("admin"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Permission denied. Only logged-in users and admins can view stocks.");
        }

        Map<String, Stock> stocks = InMemoryDatabase.getStockDB();
        return ResponseEntity.ok(stocks);
    }
}
