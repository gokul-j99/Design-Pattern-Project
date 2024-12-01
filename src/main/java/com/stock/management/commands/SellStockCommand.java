package com.stock.management.commands;

import com.stock.management.bridge.Currency;
import com.stock.management.models.Portfolio;
import com.stock.management.models.Stock;
import com.stock.management.storage.InMemoryDatabase;

public class SellStockCommand implements Command {
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

        portfolioStock.setQuantity(portfolioStock.getQuantity() - quantity);
        if (portfolioStock.getQuantity() == 0) {
            portfolio.removeStock(portfolioStock);
        }
        InMemoryDatabase.savePortfolio(portfolio.getName(), portfolio);
        InMemoryDatabase.updateStockQuantity(stock.getName(), quantity);
    }

    @Override
    public void undo() {
        portfolio.addStock(stock);
        InMemoryDatabase.savePortfolio(portfolio.getName(), portfolio);
        InMemoryDatabase.updateStockQuantity(stock.getName(), -quantity);
        System.out.println("Undo: Re-added " + quantity + " of stock '" + stock.getName() + "' to portfolio.");
    }
}
