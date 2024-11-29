package com.stock.management.decorator;

import com.stock.management.models.Stock;

public class DetailedStockReport extends StockDecorator {
    public DetailedStockReport(Stock stock) {
        super(stock);
    }

    @Override
    public String display() {
        return stock.display() + " [Detailed Report: Performance, Market Trends, etc.]";
    }
}
