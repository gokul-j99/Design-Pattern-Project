package com.stock.management.controllers;

import com.stock.management.observer.Observer;
import com.stock.management.observer.StockNotifier;
import com.stock.management.observer.UserObserver;
import com.stock.management.storage.InMemoryDatabase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class StockNotifierController {

    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribe(@RequestParam String username, @RequestParam String stockName) {
        try {
            UserObserver userObserver = new UserObserver(username);
            StockNotifier.getInstance().addObserver(userObserver);
            InMemoryDatabase.subscribeObserver(stockName, userObserver);
            return ResponseEntity.ok(username + " subscribed to notifications for stock: " + stockName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to subscribe user " + username + " for stock: " + stockName + ". Error: " + e.getMessage());
        }
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<String> unsubscribe(@RequestParam String username, @RequestParam String stockName) {
        try {
            UserObserver userObserver = new UserObserver(username);
            InMemoryDatabase.unsubscribeObserver(stockName, userObserver);
            return ResponseEntity.ok(username + " unsubscribed from notifications for stock: " + stockName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to unsubscribe user " + username + " for stock: " + stockName + ". Error: " + e.getMessage());
        }
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<Map<String, List<String>>> viewSubscriptions() {
        try {
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
            return ResponseEntity.ok(subscriptions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Return null body with 500 status
        }
    }

    @GetMapping("/user-notifications")
    public ResponseEntity<Object> getUserNotifications(
            @RequestParam String username,
            @RequestParam(required = false) String stockName) {
        try {
            for (List<Observer> observers : InMemoryDatabase.getAllSubscriptions().values()) {
                for (Observer observer : observers) {
                    if (observer instanceof UserObserver && ((UserObserver) observer).getUsername().equals(username)) {
                        if (stockName == null) {
                            return ResponseEntity.ok(((UserObserver) observer).getNotificationLog());
                        } else {
                            return ResponseEntity.ok(((UserObserver) observer).filterNotifications(stockName));
                        }
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No notifications found for user: " + username);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve notifications for user " + username + ". Error: " + e.getMessage());
        }
    }
}
