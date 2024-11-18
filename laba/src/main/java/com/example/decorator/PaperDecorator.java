package com.example.decorator;

public class PaperDecorator extends AbstractDecorator {
    private static final int OFFPRICE = 13;

    public PaperDecorator(Item item) {
        super(item);
    }

    @Override
    public String getDescription() {
        return getItem().getDescription() + " in a paper wrapper!";
    }

    @Override
    public double price() {
        return OFFPRICE + getItem().price();
    }
}