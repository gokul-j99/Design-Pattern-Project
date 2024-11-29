package com.stock.management.Utility;

import com.stock.management.models.Portfolio;
import com.stock.management.storage.InMemoryDatabase;

import java.util.Set;

public class StockHelper {

    public static Portfolio getValidPortfolio(String username, String portfolioName){
        Set<Portfolio> portfolios = InMemoryDatabase.getPortfolios(username);
        Portfolio portfolio = null;
        for (Portfolio p : portfolios) {
            if (p.getName().equals(portfolioName)) {
                portfolio = p;
                break;
            }
        }
        return  portfolio;
    }
}
