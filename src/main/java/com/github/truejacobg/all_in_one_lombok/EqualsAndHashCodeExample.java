package com.github.truejacobg.all_in_one_lombok;

import com.github.truejacobg.all_in_one_lombok.to_string.Shape;
import com.github.truejacobg.all_in_one_lombok.to_string.ToStringExample;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class EqualsAndHashCodeExample {
    private transient int transientVar = 10;
    private String name;
    private double score;

    @EqualsAndHashCode.Exclude
    private Shape shape = new ToStringExample.Square(5, 10);

    private String[] tags;

    @EqualsAndHashCode.Exclude
    private int id;

    public String getName() {
        return this.name;
    }
}
