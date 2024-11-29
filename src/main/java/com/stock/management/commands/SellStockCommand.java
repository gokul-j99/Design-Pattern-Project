package com.stock.management.commands;

import com.stock.management.models.Portfolio;
import com.stock.management.models.Stock;
import com.stock.management.storage.InMemoryDatabase;

public class SellStockCommand implements Command {
    private Portfolio portfolio;
    private Stock stock;
    private int quantity;

    public SellStockCommand(Portfolio portfolio, Stock stock, int quantity) {
        this.portfolio = portfolio;
        this.stock = stock;
        this.quantity = quantity;
    }

    @Override
    public void execute() {
        // Validate if stock exists in portfolio
        Stock portfolioStock = portfolio.getStockByName(stock.getName());
        if (portfolioStock == null) {
            System.out.println("Transaction failed: Stock '" + stock.getName() + "' not found in portfolio.");
            return;
        }

        // Check if the user has enough stock to sell
        if (portfolioStock.getQuantity() < quantity) {
            System.out.println("Transaction failed: Not enough stock quantity to sell. Owned: " +
                    portfolioStock.getQuantity() + ", Requested: " + quantity);
            return;
        }

        // Update portfolio: Reduce stock quantity or remove stock
        portfolioStock.setQuantity(portfolioStock.getQuantity() - quantity);
        if (portfolioStock.getQuantity() == 0) {
            portfolio.removeStock(portfolioStock);
        }
        InMemoryDatabase.savePortfolio(portfolio.getName(), portfolio);

        // Update stock database: Increase available stock quantity
        InMemoryDatabase.updateStockQuantity(stock.getName(), quantity);

        System.out.println("Executed: Sold " + quantity + " of stock '" + stock.getName() + "'.");
    }

    @Override
    public void undo() {
        // Add the stock back to the portfolio
        Stock portfolioStock = portfolio.getStockByName(stock.getName());
        if (portfolioStock == null) {
            portfolio.addStock(new Stock(stock.getName(), stock.getPrice(), quantity) {
                @Override
                public String display() {
                    return "Stock: " + getName() + ", Price: " + getPrice();
                }
            });
        } else {
            portfolioStock.setQuantity(portfolioStock.getQuantity() + quantity);
        }
        InMemoryDatabase.savePortfolio(portfolio.getName(), portfolio);

        // Reduce the stock quantity in the database
        InMemoryDatabase.updateStockQuantity(stock.getName(), -quantity);

        System.out.println("Undone: Re-added " + quantity + " of stock '" + stock.getName() + "' to portfolio.");
    }
}
