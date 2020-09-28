package com.bilyoner.coupon.models;


public enum EventTypes {
    Football(1),
    BasketBall(2),
    Tennis(3);
    
    private final int value;

    private EventTypes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
