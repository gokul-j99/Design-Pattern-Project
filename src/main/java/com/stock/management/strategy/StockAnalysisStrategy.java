package com.stock.management.strategy;

import com.stock.management.models.Stock;
import com.stock.management.models.UserStock;

public interface StockAnalysisStrategy {
    String analyze(UserStock userStock);
}
