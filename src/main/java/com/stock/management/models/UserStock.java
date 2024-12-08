package com.stock.management.models;

public class UserStock {

    private String user;
    private String Stock;

    public UserStock(String user, String stock) {
        this.user = user;
        Stock = stock;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getStock() {
        return Stock;
    }

    public void setStock(String stock) {
        Stock = stock;
    }
}
