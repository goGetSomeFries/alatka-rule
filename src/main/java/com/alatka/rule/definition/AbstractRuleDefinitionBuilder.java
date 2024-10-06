package com.alatka.rule.definition;

import com.alatka.rule.context.RuleDefinition;
import com.alatka.rule.context.RuleDefinitionContext;
import com.alatka.rule.context.RuleGroupDefinition;

import java.util.List;

public abstract class AbstractRuleDefinitionBuilder<T> implements RuleDefinitionBuilder {

    @Override
    public void build() {
        RuleDefinitionContext context = RuleDefinitionContext.getInstance();

        this.getSources().forEach(source -> {
            RuleGroupDefinition ruleGroupDefinition = this.buildRuleGroupDefinition(source);
            List<RuleDefinition> ruleDefinitions = this.buildRuleDefinitions(source);
            context.setRuleGroup(ruleGroupDefinition, ruleDefinitions);
        });
    }

    @Override
    public void refresh() {

    }

    protected abstract List<T> getSources();

    protected abstract RuleGroupDefinition buildRuleGroupDefinition(T source);

    protected abstract List<RuleDefinition> buildRuleDefinitions(T source);
}
