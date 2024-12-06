package com.stock.management.bridge;

public class Euro implements Currency {
    private static final double EUR_TO_USD_RATE = 1.1;

    @Override
    public double convertToBaseCurrency(double amount) {
        return amount * EUR_TO_USD_RATE; // Convert EUR to USD
    }

    @Override
    public String getCurrencySymbol() {
        return "EUR";
    }

    @Override
    public String toString() {
        return "Euro";
    }
}
