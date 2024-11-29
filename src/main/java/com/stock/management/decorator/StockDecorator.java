package com.stock.management.decorator;

import com.stock.management.models.Stock;

public abstract class StockDecorator extends Stock {
    protected Stock stock;

    public StockDecorator(Stock stock) {
        super(stock.getName(), stock.getPrice());
        this.stock = stock;
    }

    public abstract String display();
}
