package com.stock.management.template;

import com.stock.management.models.Stock;

import java.util.ArrayList;
import java.util.List;

public class FileStockDataProcessor extends StockDataTemplate {
    @Override
    protected void connectToSource() {
        System.out.println("Opening stock data file...");
    }

    @Override
    protected List<Stock> fetchStockDetails() {
        System.out.println("Reading stock details from file...");

        List<Stock> stocks = new ArrayList<>();
        stocks.add(new Stock("Apple", 150.0, 25) {
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
