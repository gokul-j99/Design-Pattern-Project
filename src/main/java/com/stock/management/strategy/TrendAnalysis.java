package com.stock.management.strategy;

import com.stock.management.models.Stock;
import com.stock.management.storage.InMemoryDatabase;

import java.util.List;

public class TrendAnalysis implements StockAnalysisStrategy {
    public String analyze(Stock stock) {
        List<Double> currentPrice = InMemoryDatabase.getStockPrice(stock.getName());
        double previousPrice = 0;
        int performance = 0;
        int index = Math.max(0,currentPrice.size() - 6);
        int n = currentPrice.size();
        for(double price : currentPrice.subList(index, n)){

            if (price > previousPrice) {
                performance+=2;
            } else {
                performance-=2;
            }
            previousPrice = price;

        }

        if (performance > 0) {
            return stock.getName() + ": Uptrend (Current Price: " + currentPrice.get(n-1) + ")";
        } else if (performance < 0) {
            return stock.getName() + ": Downtrend (Current Price: " + currentPrice.get(n-1) + ")";
        } else {
            return stock.getName() + ": No significant change.";
        }
    }
}
