package com.stock.management.decorator;

import com.stock.management.models.Stock;
import com.stock.management.models.UserStock;
import com.stock.management.storage.InMemoryDatabase;
import com.stock.management.strategy.AnalysisContext;
import com.stock.management.strategy.TrendAnalysis;

public class DetailedStockReport extends StockDecorator {
    public DetailedStockReport(UserStock stock) {
        super(stock);
    }

    @Override
    public String display() {

        AnalysisContext context = new AnalysisContext();
        context.setStrategy(new TrendAnalysis());
        Stock stocks = InMemoryDatabase.getStock(stock.getStock());

        return stocks.display() + context.executeAnalysis(stock);
    }
}
