package com.stock.management.controllers;

import com.stock.management.Utility.StockHelper;
import com.stock.management.commands.BuyStockCommand;
import com.stock.management.commands.SellStockCommand;
import com.stock.management.commands.TransactionManager;
import com.stock.management.models.Portfolio;
import com.stock.management.models.Stock;
import com.stock.management.storage.InMemoryDatabase;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private TransactionManager transactionManager = new TransactionManager();

    @PostMapping("/buy")
    public String buyStock(
            @RequestParam String username,
            @RequestParam String stockName,
            @RequestParam String portfolioName,
            @RequestParam int quantity) {

        Portfolio portfolio = StockHelper.getValidPortfolio(username,portfolioName);
        if (portfolio == null) {
            return "No portfolio named '" + portfolioName + "' found for user: " + username;
        }
        Stock stock = InMemoryDatabase.getStock(stockName);
        if (stock == null) {
            return "Stock not found in database: " + stockName;
        }

        int availableQuantity = InMemoryDatabase.getAvailableQuantity(stockName);
        if (quantity > availableQuantity) {
            return "Transaction failed: Not enough stock available. Requested: " +
                    quantity + ", Available: " + availableQuantity;
        }

        BuyStockCommand command = new BuyStockCommand(portfolio, stock, quantity);
        transactionManager.executeCommand(command);
        return "Stock " + stockName + " bought. Remaining available: " +
                InMemoryDatabase.getAvailableQuantity(stockName);
    }

    @PostMapping("/undo")
    public String undoLastTransaction() {
        transactionManager.undoLastCommand();
        return "Last transaction undone.";
    }


    @PostMapping("/sell")
    public String sellStock(
            @RequestParam String username,
            @RequestParam String stockName,
            @RequestParam String portfolioName,
            @RequestParam int quantity) {
        // Validate portfolio

        Portfolio portfolio = StockHelper.getValidPortfolio(username,portfolioName);

        if (portfolio == null) {
            return "No portfolio named '" + portfolioName + "' found for user: " + username;
        }

        // Check if stock exists in portfolio
        Stock stock = InMemoryDatabase.getStock(stockName);
        if (stock == null) {
            return "Stock '" + stockName + "' not found in database.";
        }

        // Create and execute the SellStockCommand
        SellStockCommand command = new SellStockCommand(portfolio, stock, quantity);
        transactionManager.executeCommand(command);

        return "Stock '" + stockName + "' sold. Remaining quantity in portfolio: " +
                (portfolio.getStockByName(stockName) != null
                        ? portfolio.getStockByName(stockName).getQuantity()
                        : 0);
    }
}
