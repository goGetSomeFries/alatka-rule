package com.alatka.rule.aviator.function;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * datetime.offset(v_datetime, 2, 'Days')
 * datetime.offset(v_datetime, -1, 'Hours')
 *
 * @author whocares
 * @see ChronoUnit
 */
public class LocalDateTimeOffsetFunction extends AbstractFunction {

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {
        LocalDateTime dateTime = (LocalDateTime) FunctionUtils.getJavaObject(arg1, env);
        Number offset = FunctionUtils.getNumberValue(arg2, env);
        String unit = FunctionUtils.getStringValue(arg3, env);
        LocalDateTime result = dateTime.plus(offset.longValue(), ChronoUnit.valueOf(unit));
        return AviatorRuntimeJavaType.valueOf(result);
    }

    @Override
    public String getName() {
        return "datetime.offset";
    }
}
