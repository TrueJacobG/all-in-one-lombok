package com.github.truejacobg.all_in_one_lombok;

import lombok.AccessLevel;
import lombok.Value;
import lombok.With;
import lombok.experimental.NonFinal;

@Value
public class ValueExample {
    String name;

    @With(AccessLevel.PACKAGE)
    @NonFinal
    int age;

    double score;
    protected String[] tags;
}
