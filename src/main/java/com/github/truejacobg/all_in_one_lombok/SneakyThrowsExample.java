package com.github.truejacobg.all_in_one_lombok;

import lombok.SneakyThrows;
import java.io.UnsupportedEncodingException;

public class SneakyThrowsExample implements Runnable {
    @SneakyThrows(UnsupportedEncodingException.class)
    public String utf8ToString(byte[] bytes) {
        return new String(bytes, "UTF-8");
    }

    @SneakyThrows
    public void run() {
        throw new Throwable();
    }
}

