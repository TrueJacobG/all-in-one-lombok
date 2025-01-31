package com.github.truejacobg.all_in_one_lombok;

import lombok.Locked;

import java.util.concurrent.locks.Lock;

public class LockedExample {
    private int value = 0;
    private Lock baseLock;

    @Locked.Read
    public int getValue() {
        return value;
    }

    @Locked.Write
    public void setValue(int newValue) {
        value = newValue;
    }

    @Locked("baseLock")
    public void foo() {
        System.out.println("bar");
    }
}
