package com.stock.management.template;

import com.stock.management.models.FinanceStock;
import com.stock.management.models.Stock;
import com.stock.management.models.TechStock;

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
        stocks.add(new TechStock("Amazon",121.1,202));
        stocks.add(new FinanceStock("Statestreet",121.1,403));
        return stocks;
    }
}
