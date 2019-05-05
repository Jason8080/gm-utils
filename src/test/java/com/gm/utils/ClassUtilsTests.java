package com.gm.utils;

import com.gm.utils.base.ClassUtils;

import java.util.Set;

public class ClassUtilsTests {
    public static void main(String[] args) {
        Set<Class<?>> classes = ClassUtils.getClasses("org");
        System.out.println(classes.size());
    }
}
