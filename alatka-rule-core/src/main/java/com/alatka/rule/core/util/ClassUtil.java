package com.alatka.rule.core.util;

/**
 * @author ybliu
 */
public class ClassUtil {

    public static Class<?> forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
