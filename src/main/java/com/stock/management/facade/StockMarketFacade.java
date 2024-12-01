package com.stock.management.facade;

import com.stock.management.Utility.StockHelper;
import com.stock.management.commands.BuyStockCommand;
import com.stock.management.commands.SellStockCommand;
import com.stock.management.commands.TransactionManager;
import com.stock.management.models.Portfolio;
import com.stock.management.models.Stock;
import com.stock.management.storage.InMemoryDatabase;

public class StockMarketFacade {
    private TransactionManager transactionManager;

    public StockMarketFacade() {
        this.transactionManager = new TransactionManager();
    }

    public String buyStock(String username, String stockName, String portfolioName, int quantity) {
        // Fetch the user's portfolio
        Portfolio portfolio = StockHelper.getValidPortfolio(username,portfolioName);
        if (portfolio == null) {
            return "No portfolio named '" + portfolioName + "' found for user: " + username;
        }

        // Fetch the stock details
        Stock stock = InMemoryDatabase.getStock(stockName);
        if (stock == null) {
            return "Stock '" + stockName + "' not found in database.";
        }

        // Check stock availability
        int availableQuantity = InMemoryDatabase.getAvailableQuantity(stockName);
        if (quantity > availableQuantity) {
            return "Transaction failed: Not enough stock available. Requested: " + quantity +
                    ", Available: " + availableQuantity;
        }

        // Execute BuyStockCommand
        BuyStockCommand buyCommand = new BuyStockCommand(portfolio, stock, quantity);
        transactionManager.executeCommand(username,buyCommand);

        return "Stock '" + stockName + "' bought. Remaining available: " +
                InMemoryDatabase.getAvailableQuantity(stockName);
    }

    public String sellStock(String username, String stockName, String portfolioName, int quantity) {
        // Fetch the user's portfolio
        Portfolio portfolio = StockHelper.getValidPortfolio(username,portfolioName);
        if (portfolio == null) {
            return "No portfolio named '" + portfolioName + "' found for user: " + username;
        }

        // Fetch the stock details
        Stock stock = InMemoryDatabase.getStock(stockName);
        if (stock == null) {
            return "Stock '" + stockName + "' not found in database.";
        }

        // Execute SellStockCommand
        SellStockCommand sellCommand = new SellStockCommand(portfolio, stock, quantity);
        transactionManager.executeCommand(username,sellCommand);

        return "Stock '" + stockName + "' sold. Remaining in portfolio: " +
                (portfolio.getStockByName(stockName) != null
                        ? portfolio.getStockByName(stockName).getQuantity()
                        : 0);
    }

    public String checkStockDetails(String stockName) {
        Stock stock = InMemoryDatabase.getStock(stockName);
        if (stock == null) {
            return "Stock '" + stockName + "' not found in database.";
        }
        return "Stock Details: " + stock.display() +
                ", Available Quantity: " + InMemoryDatabase.getAvailableQuantity(stockName);
    }
}
