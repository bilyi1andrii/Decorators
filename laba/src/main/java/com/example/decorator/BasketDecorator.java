package com.example.decorator;

public class BasketDecorator extends AbstractDecorator {
    private static final int OFFPRICE = 4;

    public BasketDecorator(Item item) {
        super(item);
    }

    @Override
    public String getDescription() {
        return getItem().getDescription() + " in a basket wrapper!";
    }

    @Override
    public double price() {
        return OFFPRICE + getItem().price();
    }
}