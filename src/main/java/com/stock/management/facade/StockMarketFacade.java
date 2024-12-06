package com.stock.management.facade;

import com.stock.management.Utility.StockHelper;
import com.stock.management.bridge.Currency;
import com.stock.management.bridge.CurrencyFactory;
import com.stock.management.commands.BuyStockCommand;
import com.stock.management.commands.SellStockCommand;
import com.stock.management.commands.TransactionManager;
import com.stock.management.models.CurrencyType;
import com.stock.management.models.Portfolio;
import com.stock.management.models.Stock;
import com.stock.management.storage.InMemoryDatabase;

public class StockMarketFacade {
    private TransactionManager transactionManager;

    public StockMarketFacade() {
        this.transactionManager = new TransactionManager();
    }

    public String buyStock(String username, String stockName, String portfolioName, int quantity, String currencyType) {
        // Validate currency type
        CurrencyType currencyEnum = CurrencyType.fromString(currencyType);

        if(!InMemoryDatabase.isValidUser(username)){
            return "No User named '" + username + "' found ";
        }

        // Fetch the user's portfolio
        Portfolio portfolio = StockHelper.getValidPortfolio(username, portfolioName);
        if (portfolio == null) {
            return "No portfolio named '" + portfolioName + "' found for user: " + username;
        }

        // Fetch the stock details
        Stock stock = InMemoryDatabase.getStock(stockName);
        if (stock == null) {
            return "Stock '" + stockName + "' not found in database.";
        }


        // Create Currency object using factory
        Currency currency = CurrencyFactory.getCurrency(currencyEnum);

        // Execute BuyStockCommand
        BuyStockCommand buyCommand = new BuyStockCommand(portfolio, stock, quantity, currency);
        transactionManager.executeCommand(username, buyCommand);

        return "Stock '" + stockName + "' bought. Remaining available: " +
                InMemoryDatabase.getAvailableQuantity(stockName);
    }

    public String sellStock(String username, String stockName, String portfolioName, int quantity, String currencyType) {
        // Validate currency type
        CurrencyType currencyEnum = CurrencyType.fromString(currencyType);

        // Fetch the user's portfolio
        Portfolio portfolio = StockHelper.getValidPortfolio(username, portfolioName);
        if (portfolio == null) {
            return "No portfolio named '" + portfolioName + "' found for user: " + username;
        }

        // Fetch the stock details
        Stock stock = portfolio.getStockByName(stockName);
        if (stock == null) {
            return "Stock '" + stockName + "' not found in your portfolio.";
        }

        // Create Currency object using factory
        Currency currency = CurrencyFactory.getCurrency(currencyEnum);

        // Execute SellStockCommand
        SellStockCommand sellCommand = new SellStockCommand(portfolio, stock, quantity, currency);
        transactionManager.executeCommand(username, sellCommand);

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
