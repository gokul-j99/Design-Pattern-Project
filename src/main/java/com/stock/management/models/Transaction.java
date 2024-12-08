package com.stock.management.models;

public class Transaction {
    private String type; // "buy" or "sell"
    private double price;
    private int quantity;

    public Transaction(String type, double price, int quantity) {
        this.type = type;
        this.price = price;
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
