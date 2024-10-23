package com.alatka.rule.context;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

    @Disabled
    @Test
    @DisplayName("TODO")
    void test04() {
        // TODO
    }
}
