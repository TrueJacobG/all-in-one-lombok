package com.github.truejacobg.all_in_one_lombok;

import lombok.val;
// not allowed since Java 10
//import lombok.var;

import java.util.ArrayList;

public class ValExample {
    val name;

    public String example() {
        val example = new ArrayList<String>();
        example.add("Hello, World!");
        val foo = example.get(0);
        return foo.toLowerCase();
    }
}
