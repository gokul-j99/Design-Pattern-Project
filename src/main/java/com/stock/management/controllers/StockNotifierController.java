package com.stock.management.controllers;

import com.stock.management.observer.Observer;
import com.stock.management.observer.StockNotifier;
import com.stock.management.observer.UserObserver;
import com.stock.management.storage.InMemoryDatabase;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class StockNotifierController {

    @PostMapping("/subscribe")
    public String subscribe(@RequestParam String username, @RequestParam String stockName) {
        UserObserver userObserver = new UserObserver(username);
        StockNotifier.getInstance().addObserver(userObserver);
        InMemoryDatabase.subscribeObserver(stockName, userObserver);
        return username + " subscribed to notifications for stock: " + stockName;
    }

    @PostMapping("/unsubscribe")
    public String unsubscribe(@RequestParam String username, @RequestParam String stockName) {
        UserObserver userObserver = new UserObserver(username);
        InMemoryDatabase.unsubscribeObserver(stockName, userObserver);
        return username + " unsubscribed from notifications for stock: " + stockName;
    }

    @PostMapping("/update-price")
    public String updateStockPrice(@RequestParam String stockName, @RequestParam double newPrice) {
        String message = "Stock price updated to $" + newPrice;
        StockNotifier.getInstance().notifyObservers(stockName, message);
        InMemoryDatabase.notifyStockUpdate(stockName, message);
        return "Stock price updated for " + stockName + " to $" + newPrice;
    }

    @GetMapping("/subscriptions")
    public Map<String, List<String>> viewSubscriptions() {
        Map<String, List<String>> subscriptions = new HashMap<>();
        InMemoryDatabase.getAllSubscriptions().forEach((stock, observers) -> {
            List<String> usernames = new ArrayList<>();
            for (Observer observer : observers) {
                if (observer instanceof UserObserver) {
                    usernames.add(((UserObserver) observer).getUsername());
                }
            }
            subscriptions.put(stock, usernames);
        });
        return subscriptions;
    }

    @GetMapping("/user-notifications")
    public List<String> getUserNotifications(@RequestParam String username, @RequestParam(required = false) String stockName) {
        for (List<Observer> observers : InMemoryDatabase.getAllSubscriptions().values()) {
            for (Observer observer : observers) {
                if (observer instanceof UserObserver && ((UserObserver) observer).getUsername().equals(username)) {
                    if (stockName == null) {
                        return ((UserObserver) observer).getNotificationLog();
                    } else {
                        return ((UserObserver) observer).filterNotifications(stockName);
                    }
                }
            }
        }
        return List.of("No notifications found for user: " + username);
    }
}
