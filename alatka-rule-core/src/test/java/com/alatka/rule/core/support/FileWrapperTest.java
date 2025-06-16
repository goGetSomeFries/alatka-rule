package com.alatka.rule.core.support;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FileWrapperTest {

    @Test
    @DisplayName("constructor() getter()")
    public void test01() {
        byte[] bytes = new byte[8];
        String name = "test";
        FileWrapper fileWrapper = new FileWrapper(bytes, name);

        Assertions.assertEquals(name, fileWrapper.getName());
        Assertions.assertArrayEquals(bytes, fileWrapper.getContent());

    }
}
