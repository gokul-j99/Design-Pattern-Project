package com.stock.management.observer;

import java.util.ArrayList;
import java.util.List;

public class UserObserver implements Observer {
    private String username;
    private List<String> notificationLog = new ArrayList<>();

    public UserObserver(String username) {
        this.username = username;
    }

    @Override
    public void update(String stockName, String message) {
        String notification = "Notification for " + username + " on Stock '" + stockName + "': " + message;
        notificationLog.add(notification);
        System.out.println(notification);
    }

    public String getUsername() {
        return username;
    }

    public List<String> getNotificationLog() {
        return notificationLog;
    }

    public List<String> filterNotifications(String stockName) {
        List<String> filtered = new ArrayList<>();
        for (String log : notificationLog) {
            if (log.contains(stockName)) {
                filtered.add(log);
            }
        }
        return filtered;
    }
}
