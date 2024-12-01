package com.stock.management.bridge;

import com.stock.management.models.CurrencyType;

public class CurrencyFactory {
    public static Currency getCurrency(CurrencyType currencyType) {
        switch (currencyType) {
            case USD:
                return new Dollar();
            case EUR:
                return new Euro();
            case INR:
                return new Rupee();
            default:
                throw new IllegalArgumentException("Unsupported currency type: " + currencyType);
        }
    }
}
