package com.stock.management.models;

import java.util.Objects;

public class UserStock {

    private String user;
    private String stock;

    public UserStock(String user, String stock) {
        this.user = user;
        this.stock = stock;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserStock userStock = (UserStock) o;
        return user.equals(userStock.user) && stock.equals(userStock.stock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, stock);
    }
}
