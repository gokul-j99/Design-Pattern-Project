package com.stock.management.controllers;

import com.stock.management.decorator.DetailedStockReport;
import com.stock.management.factory.StockFactory;
import com.stock.management.models.Stock;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class StockReportController {

    @PostMapping("/detailed")
    public String getDetailedReport(@RequestParam String type, @RequestParam String name, @RequestParam double price) {
        Stock stock = StockFactory.getInstance().createStock(type, name, price,1);
        Stock decoratedStock = new DetailedStockReport(stock);
        return decoratedStock.display();
    }
}
