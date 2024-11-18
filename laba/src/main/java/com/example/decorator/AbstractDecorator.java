package com.example.decorator;

import lombok.Getter;

@Getter
public abstract class AbstractDecorator extends Item {
    private Item item;

    public AbstractDecorator(Item item) {
        this.item = item;
    }

    protected Item getItem() {
        return this.item;
    }

    @Override
    public abstract String getDescription();
}
