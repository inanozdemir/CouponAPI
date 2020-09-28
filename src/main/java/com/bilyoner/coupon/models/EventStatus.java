package com.bilyoner.coupon.models;


public enum EventStatus {
    Open(1),
    Won(2),
    Loose(3),
    Refund(4);

    private final int value;

    private EventStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
