package com.example.decorator;

public abstract class Item {

    private String description = "Class: "
    + this.getClass().getSimpleName()
    + " of flowerstore";

    public String getDescription() {
        return this.description;
    }

    public abstract double price();

}