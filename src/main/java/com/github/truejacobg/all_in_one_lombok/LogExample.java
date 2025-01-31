package com.github.truejacobg.all_in_one_lombok;

import lombok.extern.java.Log;

@Log
public class LogExample {
    public static void main(String... args) {
        log.severe("Something's wrong here");
    }
}
