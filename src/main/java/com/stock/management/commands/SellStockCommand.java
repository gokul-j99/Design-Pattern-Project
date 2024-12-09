package com.stock.management.commands;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.stock.management.bridge.Currency;
import com.stock.management.models.Portfolio;
import com.stock.management.models.Stock;
import com.stock.management.models.Transaction;
import com.stock.management.models.UserStock;
import com.stock.management.storage.InMemoryDatabase;
import com.fasterxml.jackson.annotation.JsonIgnore;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SellStockCommand implements Command {

    public final static String action = "SELL";
    private Portfolio portfolio;
    private Stock stock;
    private int quantity;
    private Currency sellCurrency;

    @JsonIgnore
    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Currency getSellCurrency() {
        return sellCurrency;
    }

    public void setSellCurrency(Currency sellCurrency) {
        this.sellCurrency = sellCurrency;
    }

    public SellStockCommand(Portfolio portfolio, Stock stock, int quantity, Currency sellCurrency) {
        this.portfolio = portfolio;
        this.stock = stock;
        this.quantity = quantity;
        this.sellCurrency = sellCurrency;
    }

    @Override
    public void execute() {
        Stock portfolioStock = portfolio.getStockByName(stock.getName());
        if (portfolioStock == null) {
            System.out.println("Transaction failed: Stock '" + stock.getName() + "' not found in portfolio.");
            return;
        }

        if (portfolioStock.getQuantity() < quantity) {
            System.out.println("Transaction failed: Not enough stock quantity to sell. Owned: " +
                    portfolioStock.getQuantity() + ", Requested: " + quantity);
            return;
        }

        double totalEarningsInBaseCurrency = stock.getPrice() * quantity;
        double totalEarningsInSellCurrency = sellCurrency.convertToBaseCurrency(totalEarningsInBaseCurrency);

        System.out.println("Selling " + quantity + " of stock '" + stock.getName() + "' for " +
                sellCurrency.getCurrencySymbol() + totalEarningsInSellCurrency +
                " (" + totalEarningsInBaseCurrency + " in base currency USD).");

        // Deduct the sold quantity from the portfolio
        portfolioStock.setQuantity(portfolioStock.getQuantity() - quantity);
        if (portfolioStock.getQuantity() == 0) {
            portfolio.removeStock(portfolioStock);
        }

        // Save updated portfolio and update available stock in the database
        InMemoryDatabase.savePortfolio(portfolio.getName(), portfolio);
        InMemoryDatabase.updateStockQuantity(stock.getName(), quantity);
        UserStock userStock = new UserStock(portfolio.getOwner(), stock.getName());
        InMemoryDatabase.recordTransaction(userStock, new Transaction("sell", stock.getPrice(), quantity));
    }

    @Override
    public void undo() {
        // Add the stock back to the portfolio
        Stock portfolioStock = portfolio.getStockByName(stock.getName());
        if (portfolioStock == null) {
            // If stock doesn't exist in the portfolio, add it
            stock.setQuantity(quantity);
            portfolio.addStock(stock);
        } else {
            // If stock exists, increment its quantity
            portfolioStock.setQuantity(portfolioStock.getQuantity() + quantity);
        }

        // Update portfolio in the database
        InMemoryDatabase.savePortfolio(portfolio.getName(), portfolio);

        // Reduce the stock quantity in the database
        boolean stockUpdated = InMemoryDatabase.updateStockQuantity(stock.getName(), -quantity);
        if (!stockUpdated) {
            System.out.println("Undo failed: Could not update stock quantity for stock: " + stock.getName());
            return;
        }

        System.out.println("Undo: Restored " + quantity + " of stock '" + stock.getName() + "' to portfolio '" +
                portfolio.getName() + "'.");

    }

    @Override
    public String toString() {
        return "SellStockCommand{" +
                "portfolio=" + portfolio.getName() +
                ", stock=" + stock.getName() +
                ", action=" + action +
                ", quantity=" + quantity +
                ", sellCurrency=" + sellCurrency.getCurrencySymbol() +
                '}';
    }
}
