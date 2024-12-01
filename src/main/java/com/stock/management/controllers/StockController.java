package com.stock.management.controllers;

import com.stock.management.factory.StockFactory;
import com.stock.management.models.Stock;
import com.stock.management.observer.StockNotifier;
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
            @RequestParam double price,
            @RequestParam int quantity,
            @RequestParam String type) {
        String role = InMemoryDatabase.getUserRole(adminName);
        if (role == null || !role.equals("admin")) {
            return "Permission denied. Only admins can add stocks.";
        }
        Stock stock = StockFactory.getInstance().createStock(type,stockName,price,quantity);

        InMemoryDatabase.saveStock(stock);
        return "Stock '" + stockName + "' added to the database.";
    }


    @PostMapping("/update")
    public String updateStockPrice( @RequestParam String adminName,@RequestParam String stockName,
                                    @RequestParam(required = false) Double newPrice,
                                    @RequestParam(required = false) Integer quantity) {
        String message = "Stock price updated to $" + newPrice;

        String role = InMemoryDatabase.getUserRole(adminName);
        if (role == null || !role.equals("admin")) {
            return "Permission denied. Only admins can add stocks.";
        }
        if(newPrice != null){
            boolean result  = InMemoryDatabase.updateStockPrice(stockName,newPrice);

            if(!result){
                return "The Given Stock" +  stockName +" is not in DB. Please add the stock.";
            }
        }
        if(quantity != null){

            boolean result  = InMemoryDatabase.updateStockQuantity(stockName,quantity);

            if(!result){
                return "The Given Stock" +  stockName +" is not in DB. Please add the stock.";
            }
        }

        InMemoryDatabase.notifyStockUpdate(stockName, message);
        StockNotifier.getInstance().notifyObservers(stockName, message);
        return "Stock price updated for " + stockName + " to $" + newPrice;
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
