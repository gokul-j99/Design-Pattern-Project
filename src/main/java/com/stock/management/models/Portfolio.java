package com.stock.management.models;

import java.util.ArrayList;
import java.util.List;

public class Portfolio {
    private String name;

    private String owner;
    private List<Stock> stocks = new ArrayList<>();


    public String getOwner() {
        return owner;
    }

    private Portfolio(PortfolioBuilder builder) {
        this.name = builder.name;
        this.owner = builder.owner;
        this.stocks = builder.stocks;
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

    public static class PortfolioBuilder {
        private String name;
        private String owner;
        private List<Stock> stocks = new ArrayList<>();

        public PortfolioBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public PortfolioBuilder setOwner(String owner) {
            this.owner = owner;
            return this;
        }

        public PortfolioBuilder addStock(Stock stock) {
            this.stocks.add(stock);
            return this;
        }

        public Portfolio build() {
            return new Portfolio(this);
        }
    }
}
