package com.github.truejacobg.all_in_one_lombok;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public class DataExample {
    private final String name;
    @Setter(AccessLevel.PACKAGE)
    private int age;
    private double score;
    private String[] tags;
}
