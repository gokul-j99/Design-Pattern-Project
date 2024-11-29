package com.stock.management.strategy;

import com.stock.management.models.Stock;

public class AnalysisContext {
    private StockAnalysisStrategy strategy;

    public void setStrategy(StockAnalysisStrategy strategy) {
        this.strategy = strategy;
    }

    public String executeAnalysis(Stock stock) {
        if (strategy == null) {
            throw new IllegalStateException("No analysis strategy set.");
        }
        return strategy.analyze(stock);
    }
}
