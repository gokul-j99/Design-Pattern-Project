package com.stock.management.decorator;

import com.stock.management.models.Stock;
import com.stock.management.strategy.AnalysisContext;
import com.stock.management.strategy.TrendAnalysis;

public class DetailedStockReport extends StockDecorator {
    public DetailedStockReport(Stock stock) {
        super(stock);
    }

    @Override
    public String display() {

        AnalysisContext context = new AnalysisContext();
        context.setStrategy(new TrendAnalysis());

        return stock.display() + context.executeAnalysis(stock);
    }
}
