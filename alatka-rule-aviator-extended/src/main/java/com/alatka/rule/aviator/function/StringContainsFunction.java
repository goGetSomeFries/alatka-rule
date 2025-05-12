package com.alatka.rule.aviator.function;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

/**
 * str.contains(v_str, '1590110')
 *
 * @author whocares
 */
public class StringContainsFunction extends AbstractFunction {

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {

        String target = FunctionUtils.getStringValue(arg1, env);
        String param = FunctionUtils.getStringValue(arg2, env);

        if (target == null || param == null) {
            return AviatorBoolean.FALSE;
        }
        return target.contains(param) ? AviatorBoolean.TRUE : AviatorBoolean.FALSE;
    }

    @Override
    public String getName() {
        return "str.contains";
    }
}
