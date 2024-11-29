package com.stock.management.storage;

import com.stock.management.commands.Command;
import com.stock.management.models.Portfolio;
import com.stock.management.models.Stock;
import com.stock.management.observer.Observer;

import java.util.*;

public class InMemoryDatabase {
    private static Map<String, Set<Portfolio>> portfolioDB = new HashMap<>();
    private static Map<String, Stock> stockDB = new HashMap<>();

    private static Stack<Command> transactionHistory = new Stack<>();

    private static Map<String, List<Double>> stockPrices = new HashMap<>();
    private static Map<String, List<Observer>> observersDB = new HashMap<>();

    private static Map<String, Integer> stockQuantity = new HashMap<>();
    private static Map<String, String> sessionDB = new HashMap<>(); // Stores active sessions with roles

    public static Set<Portfolio> getPortfolios(String username) {
        return portfolioDB.get(username);
    }

    public static void savePortfolio(String username, Portfolio portfolio) {
        if(portfolioDB.containsKey(username)){
            Set<Portfolio> portfolios = portfolioDB.get(username);
            portfolios.add(portfolio);
            portfolioDB.put(username,portfolios);
        }
        else {
            Set<Portfolio> portfolios = new HashSet<>();
            portfolios.add(portfolio);
            portfolioDB.put(username,portfolios);
        }

    }

    public static void deletePortfolio(String username) {
        portfolioDB.remove(username);
    }

    public static Stock getStock(String name) {
        return stockDB.get(name);
    }

    public static void saveStock(Stock stock) {
        if (!stockPrices.containsKey(stock.getName())) {
            stockDB.put(stock.getName(), stock);
            List<Double> prices = new ArrayList<>();
            prices.add(stock.getPrice());
            stockPrices.put(stock.getName(), prices);
            stockQuantity.put(stock.getName(), stock.getQuantity());
        } else {
            updateStockPrice(stock.getName(), stock.getPrice());
            updateStockQuantity(stock.getName(),stock.getQuantity());
        }
    }

    public static int getAvailableQuantity(String stockName) {
        return stockQuantity.getOrDefault(stockName, 0);
    }

    public static void updateStockQuantity(String stockName, int change) {
        if (stockQuantity.containsKey(stockName)) {
            int currentQuantity = stockQuantity.getOrDefault(stockName, 0);
            stockQuantity.put(stockName, currentQuantity + change);
        }
    }

    public static void updateStockPrice(String name, double newPrice) {
        if (stockPrices.containsKey(name)) {
            List<Double> prices = stockPrices.get(name);
            prices.add(newPrice);
            stockPrices.put(name, prices);
            Stock stock = stockDB.get(name);
            if (stock != null) {
                stock.setPrice(newPrice);
            }
        }
    }

    public static List<Double> getStockPrice(String name) {
        return stockPrices.getOrDefault(name, new ArrayList<>());
    }

    public static Map<String, Set<Portfolio>> getAllPortfolios() {
        return portfolioDB;
    }

    public static void subscribeObserver(String stockName, Observer observer) {
        observersDB.computeIfAbsent(stockName, k -> new ArrayList<>()).add(observer);
    }

    public static void unsubscribeObserver(String stockName, Observer observer) {
        if (observersDB.containsKey(stockName)) {
            observersDB.get(stockName).remove(observer);
        }
    }


    // Transaction Management
    public static void addTransaction(Command command) {
        transactionHistory.push(command);
    }

    public static Command getLastTransaction() {
        return transactionHistory.isEmpty() ? null : transactionHistory.pop();
    }

    public static Stack<Command> getTransactionHistory() {
        return transactionHistory;
    }

    public static Map<String, List<Observer>> getAllSubscriptions() {
        return observersDB;
    }

    public static void saveSession(String username, String role) {
        sessionDB.put(username, role);
    }

    public static void removeSession(String username) {
        sessionDB.remove(username);
    }

    public static String getUserRole(String username) {
        return sessionDB.get(username);
    }

    public static Map<String, String> getAllSessions() {
        return sessionDB;
    }


    public static Map<String, Set<Portfolio>> getPortfolioDB() {
        return portfolioDB;
    }


    public static Map<String, List<Observer>> getObserversDB() {
        return observersDB;
    }

    public static void setObserversDB(Map<String, List<Observer>> observersDB) {
        InMemoryDatabase.observersDB = observersDB;
    }

    public static Map<String, String> getSessionDB() {
        return sessionDB;
    }

    public static void setSessionDB(Map<String, String> sessionDB) {
        InMemoryDatabase.sessionDB = sessionDB;
    }

    public static Map<String, Stock> getStockDB() {
        return stockDB;
    }

    public static void setStockDB(Map<String, Stock> stockDB) {
        InMemoryDatabase.stockDB = stockDB;
    }

    public static Map<String, List<Double>> getStockPrices() {
        return stockPrices;
    }

    public static void setStockPrices(Map<String, List<Double>> stockPrices) {
        InMemoryDatabase.stockPrices = stockPrices;
    }
}
