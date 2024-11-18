package com.example.decorator;

public class Flower extends Item {
    private final double price;

    public Flower(double price) {
        this.price = price;
    }

    @Override
    public double price() {
        return this.price;
    }

    @Override
    public String getDescription() {
        return "A beautiful flower";
    }
}
