package com.alatka.rule.core.definition;

/**
 * 规则构建器，构建{@link com.alatka.rule.core.context.RuleDefinition}
 *
 * @author whocares
 */
public interface RuleDefinitionBuilder {

    /**
     * 构建{@link com.alatka.rule.core.context.RuleDefinition}到{@link com.alatka.rule.core.context.RuleGroupDefinitionContext}
     */
    void build();

    /**
     * 在线刷新{@link com.alatka.rule.core.context.RuleDefinition}配置，支持蓝绿发布
     */
    void refresh();

    /**
     * 回退上一版本
     */
    void fallback();

}

