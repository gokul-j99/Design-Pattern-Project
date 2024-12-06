package com.stock.management.bridge;

public class Dollar implements Currency {
    @Override
    public double convertToBaseCurrency(double amount) {
        return amount; // USD is the base currency
    }

    @Override
    public String getCurrencySymbol() {
        return "USD";
    }

    @Override
    public String toString() {
        return "Dollar";
    }
}
