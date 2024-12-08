package com.stock.management.commands;


import com.stock.management.bridge.Currency;
import com.stock.management.models.*;
import com.stock.management.storage.InMemoryDatabase;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class BuyStockCommand implements Command {

    public final static String action = "BUY";
    private Portfolio portfolio;
    private Stock stock;
    private int quantity;



    private Currency currency;

    @JsonIgnore
    public Portfolio getPortfolio() {
        return portfolio;
    }

    @JsonIgnore
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

    public Currency getCurrency() {
        return currency;
    }

    public String getAction(){return action;}

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BuyStockCommand(Portfolio portfolio, Stock stock, int quantity, Currency currency) {
        this.portfolio = portfolio;
        this.stock = stock.clone();
        this.quantity = quantity;
        this.currency = currency;
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
        stock.setPurchaseCurrency(CurrencyType.fromString(currency.getCurrencySymbol()));
        portfolio.addStock(stock);
        InMemoryDatabase.savePortfolio(portfolio.getName(), portfolio); // Update portfolio
        InMemoryDatabase.updateStockQuantity(stock.getName(), -quantity); // Decrement stock quantity
        System.out.println("Executed: Bought " + quantity + " of " + stock.getName());
        UserStock userStock = new UserStock(portfolio.getOwner(), stock.getName());
        InMemoryDatabase.recordTransaction(userStock, new Transaction("buy", stock.getPrice(), quantity));

    }

    @Override
    public void undo() {
        // Find the stock in the portfolio
        Stock portfolioStock = portfolio.getStockByName(stock.getName());

        if (portfolioStock == null) {
            System.out.println("Undo failed: Stock '" + stock.getName() + "' not found in portfolio '" + portfolio.getName() + "'.");
            return;
        }

        // Calculate the new quantity after undoing
        int remainingQuantity = portfolioStock.getQuantity() - quantity;

        if (remainingQuantity > 0) {
            // If the stock still has remaining quantity, update the portfolio stock quantity
            portfolioStock.setQuantity(remainingQuantity);
            System.out.println("Undo partially reverted: Reduced quantity of stock '" + stock.getName() +
                    "' in portfolio '" + portfolio.getName() + "' by " + quantity +
                    ". Remaining quantity: " + remainingQuantity + ".");
        } else {
            // If the remaining quantity is 0 or less, remove the stock from the portfolio
            portfolio.removeStock(stock);
            System.out.println("Undo completely reverted: Removed stock '" + stock.getName() +
                    "' from portfolio '" + portfolio.getName() + "'.");
        }

        // Restore the stock quantity in the database
        boolean stockRestored = InMemoryDatabase.updateStockQuantity(stock.getName(), quantity);
        if (!stockRestored) {
            System.out.println("Undo failed: Could not restore stock quantity for stock: " + stock.getName());
            return;
        }

        // Save updated portfolio
        InMemoryDatabase.savePortfolio(portfolio.getName(), portfolio);
    }


    @Override
    public String toString() {
        return "BuyStockCommand{" +
                "portfolio=" + portfolio +
                ", stock=" + stock +
                ", action=" + action +
                ", quantity=" + quantity +
                ", currency=" + currency +
                '}';
    }
}
