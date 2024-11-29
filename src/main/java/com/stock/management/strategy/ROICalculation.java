package com.stock.management.strategy;
import com.stock.management.models.Stock;
import com.stock.management.storage.InMemoryDatabase;

public class ROICalculation implements StockAnalysisStrategy {
    @Override
    public String analyze(Stock stock) {
        Stock dbStock = InMemoryDatabase.getStock(stock.getName());
        if (dbStock == null){
            return "There is No value for this Stock in Db. You are the first person to purchase this Stock";
        }
        double currentPrice = InMemoryDatabase.getStock(stock.getName()).getPrice();
        double purchaseCost = stock.getPrice() * stock.getQuantity();
        double currentValue = currentPrice * stock.getQuantity();

        double roi = ((currentValue - purchaseCost) / purchaseCost) * 100;
        return "ROI for " + stock.getName() + ": " + roi + "%";
    }

}
