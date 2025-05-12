package com.alatka.rule.aviator.function;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * date.offset(v_date, 2, 'Days')
 * date.offset(v_date, -1, 'Months')
 *
 * @author whocares
 * @see ChronoUnit
 */
public class LocalDateOffsetFunction extends AbstractFunction {

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {
        LocalDate date = (LocalDate) FunctionUtils.getJavaObject(arg1, env);
        Number offset = FunctionUtils.getNumberValue(arg2, env);
        String unit = FunctionUtils.getStringValue(arg3, env);
        LocalDate result = date.plus(offset.longValue(), ChronoUnit.valueOf(unit));
        return AviatorRuntimeJavaType.valueOf(result);
    }

    @Override
    public String getName() {
        return "date.offset";
    }
}
