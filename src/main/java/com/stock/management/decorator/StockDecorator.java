package com.stock.management.decorator;

import com.stock.management.models.Stock;
import com.stock.management.models.UserStock;
import com.stock.management.storage.InMemoryDatabase;

public abstract class StockDecorator extends Stock {
    protected UserStock stock;

    public StockDecorator(UserStock userStock) {
        super();
        this.stock = userStock;
    }

    public abstract String display();
}
