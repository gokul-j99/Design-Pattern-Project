package com.stock.management.controllers;

import com.stock.management.models.Stock;
import com.stock.management.models.UserStock;
import com.stock.management.storage.InMemoryDatabase;
import com.stock.management.strategy.AnalysisContext;
import com.stock.management.strategy.ROICalculation;
import com.stock.management.strategy.TrendAnalysis;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    @GetMapping("/stock/roi")
    public String calculateROI(@RequestParam String stockName, @RequestParam String username) {
        Stock stock = InMemoryDatabase.getStock(stockName);
        if (stock == null) {
            throw new IllegalArgumentException("Stock not found: " + stockName);
        }
        UserStock userStock = new UserStock(username,stockName);
        AnalysisContext context = new AnalysisContext();
        context.setStrategy(new ROICalculation());
        return context.executeAnalysis(userStock);
    }

    @GetMapping("/stock/trend")
    public String analyzeTrend(@RequestParam String stockName, @RequestParam String username) {
        Stock stock = InMemoryDatabase.getStock(stockName);
        if (stock == null) {
            throw new IllegalArgumentException("Stock not found: " + stockName);
        }
        AnalysisContext context = new AnalysisContext();
        context.setStrategy(new TrendAnalysis());
        UserStock userStock = new UserStock(username,stockName);
        return context.executeAnalysis(userStock);
    }
}
