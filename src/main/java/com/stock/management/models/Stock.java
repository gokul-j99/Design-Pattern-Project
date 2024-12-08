package com.stock.management.models;

import com.stock.management.bridge.Currency;


public abstract class Stock implements Cloneable{
    protected String name;
    protected double price;
    protected int quantity; // To calculate ROI

    private CurrencyType purchaseCurrency;

    public Stock(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;

    }

    public Stock(){

    }
    public Stock(String name, double price) {
        this.name = name;
        this.price = price;
        this.quantity = 1;
    }

    

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public abstract String display();

    public void setQuantity(int quantity) {

        this.quantity = quantity;


    }

    public void setName(String name) {
        this.name = name;
    }

    public CurrencyType getPurchaseCurrency() {
        return purchaseCurrency;
    }

    public void setPurchaseCurrency(CurrencyType purchaseCurrency) {
        this.purchaseCurrency = purchaseCurrency;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public Stock clone() {
        try {
            return (Stock) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone not supported for Stock", e);
        }
    }
}
