package com.stock.management.template;

import com.stock.management.models.Stock;
import com.stock.management.storage.InMemoryDatabase;

import java.util.List;

public abstract class StockDataTemplate {
    public final void fetchData() {
        connectToSource();
        List<Stock> stocks = fetchStockDetails();
        processData(stocks);
        disconnect();
    }

    protected abstract void connectToSource();

    protected abstract List<Stock> fetchStockDetails();

    protected void processData(List<Stock> stocks) {
        for (Stock stock : stocks) {
            InMemoryDatabase.saveStock(stock); // Store stock in the database
            System.out.println("Processed and saved stock: " + stock.getName());
        }
    }

    private void disconnect() {
        System.out.println("Disconnected from source.");
    }
}
