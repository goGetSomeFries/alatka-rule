package com.alatka.rule.definition;

import com.alatka.rule.context.RuleDefinition;
import com.alatka.rule.context.RuleDefinitionContext;
import com.alatka.rule.context.RuleGroupDefinition;

import java.util.List;
import java.util.Map;

public abstract class AbstractRuleDefinitionBuilder<T> implements RuleDefinitionBuilder {

    @Override
    public void build() {
        RuleDefinitionContext context = RuleDefinitionContext.getInstance();
        this.getSources().stream()
                .peek(this::preProcess)
                .forEach(source -> {
                    RuleGroupDefinition ruleGroupDefinition = this.buildRuleGroupDefinition(source);
                    List<RuleDefinition> ruleDefinitions = this.buildRuleDefinitions(source);
                    context.setRuleGroup(ruleGroupDefinition, ruleDefinitions);
                });
    }

    @Override
    public void refresh() {

    }

    protected <T> T getValueWithMap(Map<String, Object> map, String key) {
        return (T) map.get(key);
    }

    protected <T> T getValueWithMap(Map<String, Object> map, String key, T defaultValue) {
        return (T) map.getOrDefault(key, defaultValue);
    }

    protected abstract List<T> getSources();

    protected abstract void preProcess(T source);

    protected abstract RuleGroupDefinition buildRuleGroupDefinition(T source);

    protected abstract List<RuleDefinition> buildRuleDefinitions(T source);
}
