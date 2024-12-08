package com.stock.management.strategy;

import com.stock.management.models.Stock;
import com.stock.management.models.UserStock;

public class AnalysisContext {
    private StockAnalysisStrategy strategy;

    public void setStrategy(StockAnalysisStrategy strategy) {
        this.strategy = strategy;
    }

    public String executeAnalysis(UserStock userStock) {
        if (strategy == null) {
            throw new IllegalStateException("No analysis strategy set.");
        }
        return strategy.analyze(userStock);
    }
}
