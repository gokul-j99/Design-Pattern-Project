package com.stock.management.models;

public class TechStock extends Stock {
    public TechStock(String name, double price, int quantity) {
        super(name, price,quantity);
    }

    @Override
    public String display() {
        return "Tech Stock: " + name + ", Price: $" + price;
    }
}
