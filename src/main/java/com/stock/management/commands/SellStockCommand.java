package com.stock.management.commands;

import com.stock.management.bridge.Currency;
import com.stock.management.models.Portfolio;
import com.stock.management.models.Stock;
import com.stock.management.storage.InMemoryDatabase;

public class SellStockCommand implements Command {

    public final static String action = "SELL";
    private Portfolio portfolio;
    private Stock stock;
    private int quantity;
    private Currency sellCurrency;

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
