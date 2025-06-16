package com.alatka.rule.core.support.aviator.function;

import com.alatka.rule.core.support.InnerConstant;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorNil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class ExitFunctionTest {

    @Test
    @DisplayName("getName()")
    public void test01() {
        ExitFunction function = new ExitFunction();
        Assertions.assertEquals("alatka.exit", function.getName());
    }

    @Test
    @DisplayName("call()")
    public void test02() {
        ExitFunction function = new ExitFunction();
        Map<String, Object> env = new HashMap<>();

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> function.call(env, new AviatorDouble(10)), "Argument must be boolean");
        function.call(env, AviatorBoolean.TRUE);
        Assertions.assertEquals(true, env.get(InnerConstant.META_EXIT_FLAG));
        function.call(env, AviatorBoolean.FALSE);
        Assertions.assertEquals(false, env.get(InnerConstant.META_EXIT_FLAG));
        Assertions.assertEquals(AviatorNil.NIL, function.call(env, AviatorBoolean.FALSE));
    }
}
