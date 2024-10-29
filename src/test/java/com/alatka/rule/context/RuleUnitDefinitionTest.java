package com.alatka.rule.context;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RuleUnitDefinitionTest {

    @Test
    @DisplayName("getter setter")
    void test01() {
        int index = 1;
        boolean enabled = false;
        String expression = "1 == 2";
        RuleDataSourceDefinition dataSourceRef = new RuleDataSourceDefinition();
        RuleUnitDefinition next = new RuleUnitDefinition();

        RuleUnitDefinition definition = new RuleUnitDefinition();
        definition.setIndex(index);
        definition.setEnabled(enabled);
        definition.setExpression(expression);
        definition.setDataSourceRef(dataSourceRef);
        definition.setNext(next);

        Assertions.assertEquals(index, definition.getIndex());
        Assertions.assertEquals(enabled, definition.isEnabled());
        Assertions.assertEquals(expression, definition.getExpression());
        Assertions.assertSame(dataSourceRef, definition.getDataSourceRef());
        Assertions.assertSame(next, definition.getNext());
    }

    @Test
    @DisplayName("字段默认值")
    void test02() {
        RuleUnitDefinition definition = new RuleUnitDefinition();

        Assertions.assertEquals(0, definition.getIndex());
        Assertions.assertTrue(definition.isEnabled());
        Assertions.assertNull(definition.getExpression());
        Assertions.assertNull(definition.getDataSourceRef());
        Assertions.assertNull(definition.getNext());
    }
}
