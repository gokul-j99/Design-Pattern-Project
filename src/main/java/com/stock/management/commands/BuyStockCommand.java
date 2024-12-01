package com.stock.management.commands;

import com.stock.management.models.Portfolio;
import com.stock.management.models.Stock;
import com.stock.management.storage.InMemoryDatabase;

public class BuyStockCommand implements Command {
    private Portfolio portfolio;
    private Stock stock;
    private int quantity;

    public BuyStockCommand(Portfolio portfolio, Stock stock, int quantity) {
        this.portfolio = portfolio;
        this.stock = stock.clone();
        this.quantity = quantity;
    }

    @Override
    public void execute() {
        int availableQuantity = InMemoryDatabase.getAvailableQuantity(stock.getName());
        if (quantity > availableQuantity) {
            System.out.println("Transaction failed: Not enough stock available. Requested: " +
                    quantity + ", Available: " + availableQuantity);
            return;
        }
        stock.setQuantity(quantity);
        portfolio.addStock(stock);
        InMemoryDatabase.savePortfolio(portfolio.getName(), portfolio); // Update portfolio
        InMemoryDatabase.updateStockQuantity(stock.getName(), -quantity); // Decrement stock quantity
        System.out.println("Executed: Bought " + quantity + " of " + stock.getName());
    }

    @Override
    public void undo() {
        portfolio.removeStock(stock);
        InMemoryDatabase.savePortfolio(portfolio.getName(), portfolio); // Update portfolio
        InMemoryDatabase.updateStockQuantity(stock.getName(), quantity); // Restore stock quantity
        System.out.println("Undone: Removed " + quantity + " of " + stock.getName());
    }
}
