package com.github.truejacobg.all_in_one_lombok;

import lombok.With;
import lombok.NonNull;
import lombok.AccessLevel;

public class WithExample {
    @With(AccessLevel.PROTECTED)
    @NonNull
    private final String name;

    @With
    private final int age;

    public WithExample(@NonNull String name, int age) {
        this.name = name;
        this.age = age;
    }
}
