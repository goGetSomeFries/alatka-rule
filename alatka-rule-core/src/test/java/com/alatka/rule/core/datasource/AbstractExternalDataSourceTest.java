package com.alatka.rule.core.datasource;

import com.alatka.rule.core.context.RuleDataSourceDefinition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AbstractExternalDataSourceTest {

    @Test
    @DisplayName("getWithConfig()")
    void test01() {
        DefaultExternalDataSource externalDataSource = new DefaultExternalDataSource();
        Assertions.assertThrows(IllegalArgumentException.class, () -> externalDataSource.getWithConfig(Collections.EMPTY_MAP, "card"), "card is required");
        Assertions.assertThrows(IllegalArgumentException.class, () -> externalDataSource.getWithConfig(Collections.singletonMap("card", ""), "card"), "card is required");
        Assertions.assertEquals("abc", externalDataSource.getWithConfig(Collections.singletonMap("card", "abc"), "card"));
    }

    @Test
    @DisplayName("buildContext()")
    void test02() {
        DefaultExternalDataSource externalDataSource = Mockito.spy(new DefaultExternalDataSource());
        Mockito.when(externalDataSource.doBuildContext(Mockito.any(), Mockito.any())).thenReturn("who");

        RuleDataSourceDefinition definition = new RuleDataSourceDefinition();
        definition.setScope(RuleDataSourceDefinition.Scope.rule);
        Map<String, Object> paramContext = new HashMap<>();
        Map<String, Object> result1 = externalDataSource.buildContext(definition, paramContext, Collections.EMPTY_MAP);
        Assertions.assertNotSame(result1, paramContext);

        definition.setScope(RuleDataSourceDefinition.Scope.request);
        Map<String, Object> result2 = externalDataSource.buildContext(definition, paramContext, Collections.EMPTY_MAP);
        Assertions.assertSame(result2, paramContext);

        definition.setId("tradeDetail");
        Map<String, Object> result3 = externalDataSource.buildContext(definition, paramContext, Collections.EMPTY_MAP);
        Assertions.assertEquals("who", result3.get("tradeDetail"));

        paramContext.put("tradeDetail", "cares");
        Map<String, Object> result4 = externalDataSource.buildContext(definition, paramContext, Collections.EMPTY_MAP);
        Assertions.assertEquals("cares", result4.get("tradeDetail"));

        definition.setScope(RuleDataSourceDefinition.Scope.global);
        definition.setId("what");

        Map<String, Object> globalScopeData = new HashMap<>();
        Map<String, Object> result5 = externalDataSource.buildContext(definition, paramContext, globalScopeData);
        Assertions.assertSame(result5, paramContext);
        Assertions.assertEquals("who", result5.get("what"));
        Assertions.assertEquals("who", globalScopeData.get("what"));
    }
}
