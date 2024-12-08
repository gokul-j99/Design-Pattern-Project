package com.stock.management.strategy;
import com.stock.management.models.Transaction;
import com.stock.management.models.UserStock;
import com.stock.management.storage.InMemoryDatabase;

import java.util.List;

public class ROICalculation implements StockAnalysisStrategy {

    @Override
    public String analyze(UserStock userStock) {
        List<Transaction> history = InMemoryDatabase.getTransaction(userStock);
        if (history.isEmpty()) {
            return "No transaction history available for stock: " + userStock.getStock();
        }

        double totalPurchaseCost = 0.0;
        int totalQuantityOwned = 0;

        // Process transaction history
        for (Transaction transaction : history) {
            if (transaction.getType().equals("buy")) {
                totalPurchaseCost += transaction.getPrice() * transaction.getQuantity();
                totalQuantityOwned += transaction.getQuantity();
            } else if (transaction.getType().equals("sell")) {
                totalPurchaseCost -= transaction.getPrice() * transaction.getQuantity();
                totalQuantityOwned -= transaction.getQuantity();
            }
        }

        if (totalQuantityOwned <= 0) {
            return "No stocks currently owned for: " + userStock.getStock();
        }

        double currentPrice = InMemoryDatabase.getStock(userStock.getStock()).getPrice();
        double currentValue = currentPrice * totalQuantityOwned;

        double roi = ((currentValue - totalPurchaseCost) / totalPurchaseCost) * 100;

        return String.format(
                "ROI for %s: %.2f%%. Total Quantity Owned: %d, Current Value: $%.2f, Total Purchase Cost: $%.2f",
                userStock.getStock(), roi, totalQuantityOwned, currentValue, totalPurchaseCost);
    }

}
