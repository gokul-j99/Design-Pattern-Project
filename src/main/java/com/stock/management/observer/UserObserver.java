package com.stock.management.observer;

public class UserObserver implements Observer {
    private String username;

    public UserObserver(String username) {
        this.username = username;
    }

    @Override
    public void update(String message) {
        System.out.println("Notification for " + username + ": " + message);
    }

    public String getUsername() {
        return username;
    }
}
