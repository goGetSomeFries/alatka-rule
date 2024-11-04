package com.alatka.rule.core.context;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class RuleParamDefinitionTest {

    @Test
    @DisplayName("getter setter")
    void test01() {
        String id = "222";
        String name = "name";
        boolean enabled = true;
        String expression = "1 == 2";

        RuleParamDefinition ruleParamDefinition = new RuleParamDefinition();
        ruleParamDefinition.setId(id);
        ruleParamDefinition.setName(name);
        ruleParamDefinition.setEnabled(enabled);
        ruleParamDefinition.setExpression(expression);

        Assertions.assertEquals(id, ruleParamDefinition.getId());
        Assertions.assertEquals(name, ruleParamDefinition.getName());
        Assertions.assertEquals(enabled, ruleParamDefinition.isEnabled());
        Assertions.assertEquals(expression, ruleParamDefinition.getExpression());
    }

    @Test
    @DisplayName("字段默认值")
    void test02() {
        RuleParamDefinition definition = new RuleParamDefinition();

        Assertions.assertNull(definition.getId());
        Assertions.assertNull(definition.getName());
        Assertions.assertTrue(definition.isEnabled());
        Assertions.assertNull(definition.getExpression());
    }

    @Test
    @DisplayName("equals()")
    void test04() {
        RuleParamDefinition definition1 = new RuleParamDefinition();
        definition1.setId("1");
        RuleParamDefinition definition2 = new RuleParamDefinition();
        definition2.setId("1");
        RuleParamDefinition definition3 = new RuleParamDefinition();
        definition3.setId("3");

        Assertions.assertEquals(definition1, definition1);
        Assertions.assertEquals(definition1, definition2);
        Assertions.assertNotEquals(definition1, definition3);
    }

    @Test
    @DisplayName("hashcode()")
    void test05() {
        RuleParamDefinition definition1 = new RuleParamDefinition();
        definition1.setId("1");
        Assertions.assertEquals(Objects.hashCode("1"), definition1.hashCode());
    }
}
