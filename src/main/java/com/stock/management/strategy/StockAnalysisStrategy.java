package com.stock.management.strategy;

import com.stock.management.models.Stock;

public interface StockAnalysisStrategy {
    String analyze(Stock stock);
}
