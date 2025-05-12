package com.alatka.rule.aviator.function;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * time.offset(v_time, 2, 'Seconds')
 * time.offset(v_time, -1, 'Hours')
 *
 * @author whocares
 * @see ChronoUnit
 */
public class LocalTimeOffsetFunction extends AbstractFunction {

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {
        LocalTime time = (LocalTime) FunctionUtils.getJavaObject(arg1, env);
        Number offset = FunctionUtils.getNumberValue(arg2, env);
        String unit = FunctionUtils.getStringValue(arg3, env);
        LocalTime result = time.plus(offset.longValue(), ChronoUnit.valueOf(unit));
        return AviatorRuntimeJavaType.valueOf(result);
    }

    @Override
    public String getName() {
        return "time.offset";
    }
}
