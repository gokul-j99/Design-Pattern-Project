package com.stock.management.models;

public abstract class Stock implements Cloneable{
    protected String name;
    protected double price;
    protected int quantity; // To calculate ROI

    public Stock(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
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

    @Override
    public Stock clone() {
        try {
            return (Stock) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone not supported for Stock", e);
        }
    }
}
