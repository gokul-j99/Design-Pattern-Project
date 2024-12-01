package com.stock.management.controllers;

import com.stock.management.Utility.StockHelper;
import com.stock.management.factory.StockFactory;
import com.stock.management.models.Portfolio;
import com.stock.management.models.Stock;
import com.stock.management.storage.InMemoryDatabase;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    @PostMapping("/create")
    public String createPortfolio(@RequestParam String username, @RequestParam String portfolioName) {
        String role = InMemoryDatabase.getUserRole(username);
        if (role == null || !role.equals("user")) {
            return "Permission denied. Only users can create portfolios.";
        }
        Portfolio portfolio = new Portfolio.PortfolioBuilder().setName(username).setOwner(username).build();
        InMemoryDatabase.savePortfolio(username, portfolio);
        return "Portfolio '" + portfolioName + "' created for user: " + username;
    }

    @PostMapping("/add-stock")
    public String addStockToPortfolio(
            @RequestParam String username,
            @RequestParam String stockName,
            @RequestParam String portfolioName,
            @RequestParam int quantity) {
        // Validate user's role
        String role = InMemoryDatabase.getUserRole(username);
        if (role == null || !role.equals("user")) {
            return "Permission denied. Only users can add stocks to portfolios.";
        }

        // Find the portfolio
        Portfolio portfolio = StockHelper.getValidPortfolio(username,portfolioName);
        if (portfolio == null) {
            return "No portfolio named '" + portfolioName + "' found for user: " + username;
        }

        // Check if the stock exists in the database
        Stock stock = InMemoryDatabase.getStock(stockName);
        if (stock == null) {
            return "Stock '" + stockName + "' not found in the database.";
        }

        // Check stock availability
        int availableQuantity = InMemoryDatabase.getAvailableQuantity(stockName);
        if (quantity > availableQuantity) {
            return "Transaction failed: Not enough stock available. Requested: " + quantity +
                    ", Available: " + availableQuantity;
        }

        // Deduct the purchased quantity from the stock database
        InMemoryDatabase.updateStockQuantity(stockName, -quantity);

        // Add the purchased stock to the user's portfolio
        Stock stockToAdd = new Stock(stock.getName(), stock.getPrice(), quantity) {
            @Override
            public String display() {
                return "Stock: " + getName() + ", Price: " + getPrice();
            }
        };
        portfolio.addStock(stockToAdd);
        InMemoryDatabase.savePortfolio(username, portfolio); // Update portfolio in the database

        return "Added stock: '" + stockToAdd.getName() + "' (Quantity: " + quantity +
                ") to portfolio '" + portfolioName + "'. Remaining available: " +
                InMemoryDatabase.getAvailableQuantity(stockName);
    }

}
