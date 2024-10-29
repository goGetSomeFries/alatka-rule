package com.alatka.rule.context;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class RuleListDefinitionTest {

    @Test
    @DisplayName("getter setter")
    void test01() {
        String id = "13";
        String name = "测试组";
        boolean enabled = false;
        RuleListDefinition.Type type = RuleListDefinition.Type.blackList;

        RuleListDefinition definition = new RuleListDefinition();
        definition.setId(id);
        definition.setName(name);
        definition.setEnabled(enabled);
        definition.setType(type);
        RuleUnitDefinition ruleUnitDefinition = new RuleUnitDefinition();
        definition.setRuleUnitDefinition(ruleUnitDefinition);

        Assertions.assertEquals(id, definition.getId());
        Assertions.assertEquals(name, definition.getName());
        Assertions.assertEquals(enabled, definition.isEnabled());
        Assertions.assertSame(type, definition.getType());
        Assertions.assertSame(ruleUnitDefinition, definition.getRuleUnitDefinition());
    }

    @Test
    @DisplayName("字段默认值")
    void test02() {
        RuleListDefinition definition = new RuleListDefinition();

        Assertions.assertNull(definition.getId());
        Assertions.assertNull(definition.getName());
        Assertions.assertTrue(definition.isEnabled());
        Assertions.assertNull(definition.getType());
        Assertions.assertNull(definition.getRuleUnitDefinition());
    }

    @Test
    @DisplayName("RuleListDefinition#Type")
    void test03() {
        RuleListDefinition.Type type1 = RuleListDefinition.Type.blackList;
        RuleListDefinition.Type type2 = RuleListDefinition.Type.whiteList;

        Assertions.assertEquals(type1, RuleListDefinition.Type.values()[0]);
        Assertions.assertEquals(type2, RuleListDefinition.Type.values()[1]);
        Assertions.assertEquals(2, RuleListDefinition.Type.values().length);
    }

    @Test
    @DisplayName("equals()")
    void test06() {
        RuleListDefinition definition1 = new RuleListDefinition();
        definition1.setId("1");
        RuleListDefinition definition2 = new RuleListDefinition();
        definition2.setId("1");
        RuleListDefinition definition3 = new RuleListDefinition();
        definition3.setId("3");

        Assertions.assertEquals(definition1, definition1);
        Assertions.assertEquals(definition1, definition2);
        Assertions.assertNotEquals(definition1, definition3);
    }

    @Test
    @DisplayName("hashcode()")
    void test07() {
        RuleListDefinition definition1 = new RuleListDefinition();
        definition1.setId("1");
        Assertions.assertEquals(Objects.hashCode("1"), definition1.hashCode());
    }
}
