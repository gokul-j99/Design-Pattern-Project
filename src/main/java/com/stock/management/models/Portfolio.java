package com.stock.management.models;

import java.util.ArrayList;
import java.util.List;

public class Portfolio {
    private String name;
    private List<Stock> stocks = new ArrayList<>();

    public Portfolio(String name) {
        this.name = name;
    }

    public void addStock(Stock stock) {
        stocks.add(stock);
    }

    public void removeStock(Stock stock) {
        stocks.remove(stock);
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public String getName() {
        return name;
    }

    public List<String> displayPortfolio() {
        List<String> stockDetails = new ArrayList<>();
        for (Stock stock : stocks) {
            stockDetails.add(stock.display());
        }
        return stockDetails;
    }

    public Stock getStockByName(String stockName){
        Stock stock = null;
        for (Stock s: stocks
             ) {
            if(s.getName().equals(stockName)){
                stock = s;
            }
        }
        return  stock;
    }
}
