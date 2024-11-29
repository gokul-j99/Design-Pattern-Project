package com.stock.management.controllers;

import com.stock.management.models.Stock;
import com.stock.management.storage.InMemoryDatabase;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    @PostMapping("/add")
    public String addStock(
            @RequestParam String adminName,
            @RequestParam String stockName,
            @RequestParam double price) {
        String role = InMemoryDatabase.getUserRole(adminName);
        if (role == null || !role.equals("admin")) {
            return "Permission denied. Only admins can add stocks.";
        }
        Stock stock = new Stock(stockName, price, 0) {
            @Override
            public String display() {
                return "Stock: " + getName() + ", Price: " + getPrice();
            }
        };
        InMemoryDatabase.saveStock(stock);
        return "Stock '" + stockName + "' added to the database.";
    }

    @GetMapping("/list")
    public Map<String, Stock> listStocks(@RequestParam String username) {
        String role = InMemoryDatabase.getUserRole(username);
        if (role == null || !(role.equals("user") || role.equals("admin"))) {
            throw new IllegalArgumentException("Permission denied. Only logged-in users and admins can view stocks.");
        }
        return InMemoryDatabase.getStockDB();
    }
}
