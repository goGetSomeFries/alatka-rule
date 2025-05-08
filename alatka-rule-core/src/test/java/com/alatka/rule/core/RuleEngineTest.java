package com.alatka.rule.core;

import com.alatka.rule.core.context.RuleDataSourceDefinition;
import com.alatka.rule.core.datasource.ExternalDataSource;
import com.alatka.rule.core.datasource.ExternalDataSourceFactory;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.ReflectionMemberAccessor;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RuleEngineTest {

    @Test
    @DisplayName("calculateExpression()")
    void test01() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Expression expression = Mockito.mock(Expression.class);
        Mockito.when(expression.execute(Mockito.any())).thenReturn(true, "1", 2, false);

        AviatorEvaluatorInstance aviatorEvaluatorInstance = Mockito.mock(AviatorEvaluatorInstance.class);
        Mockito.when(aviatorEvaluatorInstance.compile(Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean())).thenReturn(expression);

        RuleEngine ruleEngine = new RuleEngine();
        ReflectionMemberAccessor reflectionMemberAccessor = new ReflectionMemberAccessor();
        Object result1 = reflectionMemberAccessor.invoke(RuleEngine.class.getDeclaredMethod("calculateExpression", AviatorEvaluatorInstance.class, String.class, Map.class), ruleEngine, aviatorEvaluatorInstance, "", Collections.EMPTY_MAP);
        Object result2 = reflectionMemberAccessor.invoke(RuleEngine.class.getDeclaredMethod("calculateExpression", AviatorEvaluatorInstance.class, String.class, Map.class), ruleEngine, aviatorEvaluatorInstance, "", Collections.EMPTY_MAP);
        Object result3 = reflectionMemberAccessor.invoke(RuleEngine.class.getDeclaredMethod("calculateExpression", AviatorEvaluatorInstance.class, String.class, Map.class), ruleEngine, aviatorEvaluatorInstance, "", Collections.EMPTY_MAP);
        Object result4 = reflectionMemberAccessor.invoke(RuleEngine.class.getDeclaredMethod("calculateExpression", AviatorEvaluatorInstance.class, String.class, Map.class), ruleEngine, aviatorEvaluatorInstance, "", Collections.EMPTY_MAP);

        Assertions.assertEquals(true, result1);
        Assertions.assertEquals("1", result2);
        Assertions.assertEquals(2, result3);
        Assertions.assertEquals(false, result4);
    }

    @Test
    @DisplayName("extendParamContext()")
    void test02() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Map<String, Object> result = new HashMap<>(0);
        ExternalDataSource externalDataSource = Mockito.mock(ExternalDataSource.class);
        Mockito.when(externalDataSource.buildContext(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(result);

        ExternalDataSourceFactory factory = Mockito.mock(ExternalDataSourceFactory.class);
        Mockito.when(factory.getExternalDataSource(Mockito.any())).thenReturn(externalDataSource);

        MockedStatic<ExternalDataSourceFactory> mockedStatic = Mockito.mockStatic(ExternalDataSourceFactory.class);
        mockedStatic.when(ExternalDataSourceFactory::getInstance).thenReturn(factory);

        RuleEngine ruleEngine = new RuleEngine();
        ReflectionMemberAccessor reflectionMemberAccessor = new ReflectionMemberAccessor();
        Assertions.assertSame(result, reflectionMemberAccessor.invoke(RuleEngine.class.getDeclaredMethod("extendParamContext", RuleDataSourceDefinition.class, Map.class, Map.class), ruleEngine, new RuleDataSourceDefinition(), Collections.EMPTY_MAP, Collections.EMPTY_MAP));

        mockedStatic.close();
    }
}
