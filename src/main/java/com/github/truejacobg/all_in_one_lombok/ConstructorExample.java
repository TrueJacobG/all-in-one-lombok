package com.github.truejacobg.all_in_one_lombok;

import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.NonNull;

@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ConstructorExample<T> {
    private int x, y;
    @NonNull
    private T description;
    private final String finalNote;
}
