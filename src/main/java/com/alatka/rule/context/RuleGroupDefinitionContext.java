package com.alatka.rule.context;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RuleGroupDefinitionContext {

    private ConcurrentHashMap<RuleGroupDefinition, List<RuleDefinition>> ruleDefinitionsMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<RuleGroupDefinition, Set<RuleSupportDefinition>> ruleMap = new ConcurrentHashMap<>();

    public RuleGroupDefinition getRuleGroupDefinition(String ruleGroupName) {
        RuleGroupDefinition ruleGroupDefinition = new RuleGroupDefinition(ruleGroupName);
        return this.ruleDefinitionsMap.keySet().stream()
                .filter(ruleGroupDefinition::equals)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ruleGroupName + " not exists"));
    }

    public List<RuleDefinition> getRuleDefinitions(String ruleGroupName) {
        RuleGroupDefinition ruleGroupDefinition = new RuleGroupDefinition(ruleGroupName);
        List<RuleDefinition> ruleDefinitions = this.ruleDefinitionsMap.get(ruleGroupDefinition);
        if (ruleDefinitions == null) {
            throw new IllegalArgumentException(ruleGroupName + " not exists");
        }
        return ruleDefinitions;
    }

    public void initRuleDefinitions(RuleGroupDefinition ruleGroupDefinition, List<RuleDefinition> ruleDefinitions) {
        this.ruleDefinitionsMap.put(ruleGroupDefinition, ruleDefinitions);
    }

    public static RuleGroupDefinitionContext getInstance() {
        return Inner.INSTANCE;
    }

    private static class Inner {
        private static final RuleGroupDefinitionContext INSTANCE = new RuleGroupDefinitionContext();
    }
}