package com.alatka.rule.core.support.aviator.function;

import com.alatka.rule.core.support.InnerConstant;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorNil;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

public class ExitFunction extends AbstractFunction {

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        if (!(arg1 instanceof AviatorBoolean)) {
            throw new IllegalArgumentException("Argument must be boolean");
        }
        env.put(InnerConstant.META_EXIT_FLAG, ((AviatorBoolean) arg1).getBooleanValue());
        return AviatorNil.NIL;
    }

    @Override
    public String getName() {
        return "alatka.exit";
    }
}
