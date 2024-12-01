package com.stock.management.observer;

import java.util.ArrayList;
import java.util.List;

public class StockNotifier {
    private static StockNotifier instance;
    private List<Observer> observers = new ArrayList<>();

    private StockNotifier() {}

    public static StockNotifier getInstance() {
        if (instance == null) {
            instance = new StockNotifier();
        }
        return instance;
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
        System.out.println("Added observer: " + ((UserObserver) observer).getUsername());
    }

    public void notifyObservers(String stockName, String message) {
        for (Observer observer : observers) {
            observer.update(stockName, message);
        }
    }
}
