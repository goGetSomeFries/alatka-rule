package com.alatka.rule.core.support;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class InnerConstantTest {

    @Test
    @DisplayName("常量")
    public void test01() {
        Assertions.assertEquals("meta.hitResult", InnerConstant.META_HIT_RESULT);
        Assertions.assertEquals("meta.exitFlag", InnerConstant.META_EXIT_FLAG);
    }
}
