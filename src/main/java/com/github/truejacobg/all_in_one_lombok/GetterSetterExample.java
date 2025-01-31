package com.github.truejacobg.all_in_one_lombok;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class GetterSetterExample {
    @Getter
    @Setter
    private int age = 10;

    @Setter(AccessLevel.PROTECTED)
    private String name;

    @Getter(lazy = true)
    private final double[] cached = expensive();

    private double[] expensive() {
        double[] result = new double[1000000];
        for (int i = 0; i < result.length; i++) {
            result[i] = Math.asin(i);
        }
        return result;
    }
}
