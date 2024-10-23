package com.alatka.rule.context;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class RuleDefinitionTest {

    @Test
    @DisplayName("getter setter")
    void test01() {
        String id = "222";
        String name = "name";
        boolean enabled = true;
        String desc = "description";
        int priority = 20;
        int score = 100;
        RuleUnitDefinition ruleUnitDefinition = new RuleUnitDefinition();

        RuleDefinition ruleDefinition = new RuleDefinition();
        ruleDefinition.setId(id);
        ruleDefinition.setName(name);
        ruleDefinition.setEnabled(enabled);
        ruleDefinition.setDesc(desc);
        ruleDefinition.setPriority(priority);
        ruleDefinition.setScore(score);
        ruleDefinition.setRuleUnitDefinition(ruleUnitDefinition);

        Assertions.assertEquals(id, ruleDefinition.getId());
        Assertions.assertEquals(name, ruleDefinition.getName());
        Assertions.assertEquals(enabled, ruleDefinition.isEnabled());
        Assertions.assertEquals(desc, ruleDefinition.getDesc());
        Assertions.assertEquals(priority, ruleDefinition.getPriority());
        Assertions.assertEquals(score, ruleDefinition.getScore());
        Assertions.assertEquals(ruleUnitDefinition, ruleDefinition.getRuleUnitDefinition());
    }

    @Test
    @DisplayName("字段默认值")
    void test02() {
        RuleDefinition definition = new RuleDefinition();

        Assertions.assertNull(definition.getId());
        Assertions.assertNull(definition.getName());
        Assertions.assertTrue(definition.isEnabled());
        Assertions.assertNull(definition.getDesc());
        Assertions.assertEquals(0, definition.getPriority());
        Assertions.assertEquals(0, definition.getScore());
        Assertions.assertNull(definition.getRuleUnitDefinition());
    }

    @Test
    @DisplayName("toString()")
    void test03() {
        String id = "14";
        String name = "测试组";
        int priority = 20;
        int score = 100;
        RuleDefinition definition = new RuleDefinition();
        definition.setId(id);
        definition.setName(name);
        definition.setPriority(priority);
        definition.setScore(score);

        Assertions.assertEquals("{id='" + id + "', name='" + name + "', priority='" + priority + "', score='" + score + "'}", definition.toString());
    }

    @Test
    @DisplayName("equals()")
    void test05() {
        RuleDefinition definition1 = new RuleDefinition();
        definition1.setId("1");
        RuleDefinition definition2 = new RuleDefinition();
        definition2.setId("1");
        RuleDefinition definition3 = new RuleDefinition();
        definition3.setId("3");

        Assertions.assertEquals(definition1, definition1);
        Assertions.assertEquals(definition1, definition2);
        Assertions.assertNotEquals(definition1, definition3);
    }

    @Test
    @DisplayName("hashcode()")
    void test06() {
        RuleDefinition definition1 = new RuleDefinition();
        definition1.setId("1");
        Assertions.assertEquals(Objects.hashCode("1"), definition1.hashCode());
    }
}
