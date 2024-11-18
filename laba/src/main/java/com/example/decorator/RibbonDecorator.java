package com.example.decorator;

public class RibbonDecorator extends AbstractDecorator {
    private static final int OFFPRICE = 40;

    public RibbonDecorator(Item item) {
        super(item);
    }

    @Override
    public String getDescription() {
        return getItem().getDescription() + " in a ribbon wrapper!";
    }

    @Override
    public double price() {
        return OFFPRICE + getItem().price();
    }
}