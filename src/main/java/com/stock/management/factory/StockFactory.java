package com.stock.management.factory;

import com.stock.management.models.*;
import com.stock.management.storage.InMemoryDatabase;

public class StockFactory {

    // Lazy-initialized singleton instance
    private static StockFactory instance;

    // Private constructor to prevent instantiation
    private StockFactory() {}

    // Public method to access the singleton instance with lazy initialization
    public static synchronized StockFactory getInstance() {
        if (instance == null) {
            instance = new StockFactory();
        }
        return instance;
    }

    // Factory method to create stocks
    public Stock createStock(String type, String name, double price, int quantity) {
        Stock stock;
        switch (type.toLowerCase()) {
            case "tech":
                stock = new TechStock(name, price, quantity);
                InMemoryDatabase.saveStock(stock);
                return stock;
            case "finance":
                stock = new FinanceStock(name, price, quantity);
                InMemoryDatabase.saveStock(stock);
                return stock;
            default:
                throw new IllegalArgumentException("Unknown stock type");
        }
    }
}
