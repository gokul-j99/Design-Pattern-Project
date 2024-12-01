package com.stock.management.models;

public enum CurrencyType {
    USD,
    INR,
    EUR;

    public static CurrencyType fromString(String currency) {
        try {
            return CurrencyType.valueOf(currency.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid currency type: " + currency);
        }
    }
}
