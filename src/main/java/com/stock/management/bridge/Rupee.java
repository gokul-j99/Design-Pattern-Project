package com.stock.management.bridge;

public class Rupee implements Currency {
    private static final double INR_TO_USD_RATE = 0.012;

    @Override
    public double convertToBaseCurrency(double amount) {
        return amount * INR_TO_USD_RATE; // Convert INR to USD
    }

    @Override
    public String getCurrencySymbol() {
        return "₹";
    }
}
