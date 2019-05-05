package com.gm.utils;

import com.gm.utils.base.Generator;

public class GeneratorTests {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++)
            System.out.println(Generator.uuid());
    }
}
