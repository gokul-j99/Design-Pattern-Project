package com.stock.management.bridge;

public interface Currency {
    double convertToBaseCurrency(double amount); // Convert to USD
    String getCurrencySymbol();
}
