package com.github.truejacobg.all_in_one_lombok;

import lombok.Cleanup;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CleanupExample {
    public static void main(String[] args) throws IOException {
        @Cleanup InputStream in = new FileInputStream(args[0]);
        byte[] b = new byte[10000];
        while (true) {
            int r = in.read(b);
            if (r == -1) break;
        }
    }
}
