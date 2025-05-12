package com.alatka.rule.aviator.function;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorNil;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;

import java.util.Map;

/**
 * str.upperCase(v_str)
 *
 * @author whocares
 */
public class StringUpperFunction extends AbstractFunction {

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        String value = FunctionUtils.getStringValue(arg1, env);
        return value == null ? AviatorNil.NIL : new AviatorString(value.toUpperCase());
    }

    @Override
    public String getName() {
        return "str.upperCase";
    }
}
