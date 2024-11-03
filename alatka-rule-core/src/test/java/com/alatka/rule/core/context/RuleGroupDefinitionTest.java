package com.alatka.rule.core.context;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class RuleGroupDefinitionTest {

    @Test
    @DisplayName("getter setter")
    void test01() {
        String id = "13";
        String name = "测试组";
        boolean enabled = false;
        RuleGroupDefinition.Type type = RuleGroupDefinition.Type.priority_all;

        RuleGroupDefinition definition = new RuleGroupDefinition();
        definition.setId(id);
        definition.setName(name);
        definition.setEnabled(enabled);
        definition.setType(type);
        RuleListDefinition ruleListDefinition = new RuleListDefinition();
        definition.setRuleListDefinition(ruleListDefinition);

        Assertions.assertEquals(id, definition.getId());
        Assertions.assertEquals(name, definition.getName());
        Assertions.assertEquals(enabled, definition.isEnabled());
        Assertions.assertSame(type, definition.getType());
        Assertions.assertSame(ruleListDefinition, definition.getRuleListDefinition());
    }

    @Test
    @DisplayName("字段默认值")
    void test02() {
        RuleGroupDefinition definition = new RuleGroupDefinition();

        Assertions.assertNull(definition.getId());
        Assertions.assertNull(definition.getName());
        Assertions.assertTrue(definition.isEnabled());
        Assertions.assertNull(definition.getType());
        Assertions.assertNull(definition.getRuleListDefinition());
    }

    @Test
    @DisplayName("构造函数")
    void test03() {
        String id = "14";
        RuleGroupDefinition definition = new RuleGroupDefinition(id);

        Assertions.assertEquals(id, definition.getId());
    }

    @Test
    @DisplayName("toString()")
    void test04() {
        String id = "14";
        String name = "测试组";
        RuleGroupDefinition definition = new RuleGroupDefinition();
        definition.setId(id);
        definition.setName(name);

        Assertions.assertEquals("RuleGroupDefinition{id='" + id + "', " + "name='" + name + "'}", definition.toString());
    }

    @Test
    @DisplayName("RuleGroupDefinition#Type")
    void test05() {
        RuleGroupDefinition.Type type1 = RuleGroupDefinition.Type.valueOf(RuleGroupDefinition.Type.all.name());
        RuleGroupDefinition.Type type2 = RuleGroupDefinition.Type.valueOf(RuleGroupDefinition.Type.once.name());
        RuleGroupDefinition.Type type3 = RuleGroupDefinition.Type.valueOf(RuleGroupDefinition.Type.priority_all.name());
        RuleGroupDefinition.Type type4 = RuleGroupDefinition.Type.valueOf(RuleGroupDefinition.Type.priority_once.name());

        Assertions.assertEquals(type1, RuleGroupDefinition.Type.values()[0]);
        Assertions.assertEquals(type2, RuleGroupDefinition.Type.values()[1]);
        Assertions.assertEquals(type3, RuleGroupDefinition.Type.values()[2]);
        Assertions.assertEquals(type4, RuleGroupDefinition.Type.values()[3]);
        Assertions.assertEquals(4, RuleGroupDefinition.Type.values().length);
    }

    @Test
    @DisplayName("equals()")
    void test06() {
        RuleGroupDefinition definition1 = new RuleGroupDefinition();
        definition1.setId("1");
        RuleGroupDefinition definition2 = new RuleGroupDefinition();
        definition2.setId("1");
        RuleGroupDefinition definition3 = new RuleGroupDefinition();
        definition3.setId("3");

        Assertions.assertEquals(definition1, definition1);
        Assertions.assertEquals(definition1, definition2);
        Assertions.assertNotEquals(definition1, definition3);
    }

    @Test
    @DisplayName("hashcode()")
    void test07() {
        RuleGroupDefinition definition1 = new RuleGroupDefinition();
        definition1.setId("1");
        Assertions.assertEquals(Objects.hashCode("1"), definition1.hashCode());
    }
}
