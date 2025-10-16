package com.alatka.rule.core.definition;

/**
 * 规则构建器，构建{@link com.alatka.rule.core.context.RuleDefinition}
 *
 * @author whocares
 */
public interface RuleDefinitionBuilder {

    /**
     * 构建{@link com.alatka.rule.core.context.RuleDefinition}到{@link com.alatka.rule.core.context.RuleGroupDefinitionContext}
     *
     * @param ruleGroups 规则组名称，支持按指定规则组构建，默认构建全部
     */
    void build(String... ruleGroups);

    /**
     * 在线刷新{@link com.alatka.rule.core.context.RuleDefinition}配置，支持蓝绿发布
     *
     * @param ruleGroups 规则组名称，支持按指定规则组构建，默认构建全部
     */
    void refresh(String... ruleGroups);

    /**
     * 回退上一版本
     */
    void fallback();

}

