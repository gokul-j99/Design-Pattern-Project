package com.stock.management.controllers;

import com.stock.management.storage.InMemoryDatabase;
import com.stock.management.template.APIBasedStockData;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stock-data")
public class StockDataController {

    @PostMapping("/fetch")
    public String fetchStockData() {
        APIBasedStockData dataFetcher = new APIBasedStockData();
        dataFetcher.fetchData();
        return "Stock data fetched and stored in the database.";
    }

    @GetMapping("/list")
    public Map<String, String> listStockData() {
        return InMemoryDatabase.getStockDB().entrySet().stream()
                .collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue().display()), HashMap::putAll);
    }
}
