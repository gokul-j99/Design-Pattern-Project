package com.stock.management.template;

import com.stock.management.models.Stock;

import java.util.ArrayList;
import java.util.List;

public class APIBasedStockData extends StockDataTemplate {

    @Override
    protected void connectToSource() {
        System.out.println("Connecting to stock API...");
    }

    @Override
    protected List<Stock> fetchStockDetails() {
        System.out.println("Fetching stock details from API...");
        // Simulated stock data
        List<Stock> stocks = new ArrayList<>();
        stocks.add(new Stock("Apple", 150.0, 0) {
            @Override
            public String display() {
                return "Stock: " + getName() + ", Price: " + getPrice();
            }
        });
        stocks.add(new Stock("Google", 2800.0, 0) {
            @Override
            public String display() {
                return "Stock: " + getName() + ", Price: " + getPrice();
            }
        });
        return stocks;
    }
}
