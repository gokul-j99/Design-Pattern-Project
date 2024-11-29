package com.stock.management.models;

public class FinanceStock extends Stock {
    public FinanceStock(String name, double price, int quantity) {
        super(name, price,quantity);
    }

    @Override
    public String display() {
        return "Finance Stock: " + name + ", Price: $" + price;
    }
}
