package com.alatka.rule.context;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RuleGroupDefinitionContextTest {

    @Test
    @DisplayName("单例")
    void test01() {
        RuleGroupDefinitionContext context1 = RuleGroupDefinitionContext.getInstance(true);
        RuleGroupDefinitionContext context2 = RuleGroupDefinitionContext.getInstance(true);
        RuleGroupDefinitionContext context3 = RuleGroupDefinitionContext.getInstance(false);
        RuleGroupDefinitionContext context4 = RuleGroupDefinitionContext.getInstance(false);

        Assertions.assertSame(context1, context2);
        Assertions.assertSame(context3, context4);
        Assertions.assertNotSame(context1, context3);
    }

    @Test
    @DisplayName("toggle()")
    void test02() {
        RuleGroupDefinitionContext context1 = RuleGroupDefinitionContext.getInstance(true);
        RuleGroupDefinitionContext.toggle();
        RuleGroupDefinitionContext context2 = RuleGroupDefinitionContext.getInstance(false);

        Assertions.assertSame(context1, context2);
        RuleGroupDefinitionContext.toggle();
        RuleGroupDefinitionContext context3 = RuleGroupDefinitionContext.getInstance(true);
        Assertions.assertSame(context1, context3);
    }

    @Disabled
    @Test
    @DisplayName("并发toggle()")
    void test03() {
        // TODO
    }

    @Test
    @DisplayName("getAviatorEvaluatorInstance()")
    void test04() {
        RuleGroupDefinitionContext definitionContext1 = RuleGroupDefinitionContext.getInstance(true);
        Assertions.assertNotNull(definitionContext1.getAviatorEvaluatorInstance());
        RuleGroupDefinitionContext definitionContext2 = RuleGroupDefinitionContext.getInstance(true);
        Assertions.assertSame(definitionContext1, definitionContext2);
    }

    @Test
    @DisplayName("initRuleDefinitions()/getRuleDefinitions()/getRuleGroupDefinition()")
    void test05() {
        RuleGroupDefinitionContext definitionContext = RuleGroupDefinitionContext.getInstance(true);
        List<RuleDefinition> list = new ArrayList<>(0);
        RuleGroupDefinition ruleGroupDefinition = new RuleGroupDefinition("1");
        definitionContext.initRuleDefinitions(ruleGroupDefinition, list);
        Assertions.assertThrows(IllegalArgumentException.class, () -> definitionContext.initRuleDefinitions(new RuleGroupDefinition("1"), Collections.EMPTY_LIST), new RuleGroupDefinition("1") + " already exists");

        Assertions.assertSame(list, definitionContext.getRuleDefinitions("1"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> definitionContext.getRuleDefinitions("2"), "2 not exists");

        Assertions.assertSame(ruleGroupDefinition, definitionContext.getRuleGroupDefinition("1"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> definitionContext.getRuleDefinitions("2"), "2 not exists");
    }

    @Test
    @DisplayName("initRuleParamDefinitions()/getRuleParamDefinitions()")
    void test06() {
        RuleGroupDefinitionContext definitionContext = RuleGroupDefinitionContext.getInstance(true);
        List<RuleParamDefinition> list = new ArrayList<>(0);
        definitionContext.initRuleParamDefinitions(new RuleGroupDefinition("1"), list);
        Assertions.assertThrows(IllegalArgumentException.class, () -> definitionContext.initRuleParamDefinitions(new RuleGroupDefinition("1"), Collections.EMPTY_LIST), new RuleGroupDefinition("1") + " already exists");

        Assertions.assertSame(list, definitionContext.getRuleParamDefinitions("1"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> definitionContext.getRuleParamDefinitions("2"), "2 not exists");

    }

    @Test
    @DisplayName("reset()")
    void test07() {
        RuleGroupDefinitionContext definitionContext = RuleGroupDefinitionContext.getInstance(true);
        definitionContext.initRuleDefinitions(new RuleGroupDefinition("3"), Collections.EMPTY_LIST);
        definitionContext.initRuleParamDefinitions(new RuleGroupDefinition("3"), Collections.EMPTY_LIST);

        Assertions.assertEquals(0, definitionContext.getRuleParamDefinitions("3").size());
        Assertions.assertEquals(0, definitionContext.getRuleDefinitions("3").size());
        definitionContext.reset();
        Assertions.assertThrows(IllegalArgumentException.class, () -> definitionContext.getRuleParamDefinitions("3"), "3 not exists");
        Assertions.assertThrows(IllegalArgumentException.class, () -> definitionContext.getRuleDefinitions("3"), "3 not exists");
    }
}
