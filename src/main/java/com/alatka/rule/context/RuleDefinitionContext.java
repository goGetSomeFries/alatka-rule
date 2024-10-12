package com.alatka.rule.context;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class RuleDefinitionContext {

    private ConcurrentHashMap<RuleGroupDefinition, List<RuleDefinition>> ruleGroupMap = new ConcurrentHashMap<>();

    public RuleGroupDefinition getRuleGroupDefinition(String ruleGroupName) {
        RuleGroupDefinition ruleGroupDefinition = new RuleGroupDefinition(ruleGroupName);
        return this.ruleGroupMap.keySet().stream()
                .filter(ruleGroupDefinition::equals)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ruleGroupName + " not exists"));
    }

    public List<RuleDefinition> getRuleDefinitions(String ruleGroupName) {
        RuleGroupDefinition ruleGroupDefinition = new RuleGroupDefinition(ruleGroupName);
        List<RuleDefinition> ruleDefinitions = this.ruleGroupMap.get(ruleGroupDefinition);
        if (ruleDefinitions == null) {
            throw new IllegalArgumentException(ruleGroupName + " not exists");
        }
        return ruleDefinitions;
    }

    public void init(RuleGroupDefinition ruleGroupDefinition, List<RuleDefinition> ruleDefinitions) {
        this.ruleGroupMap.put(ruleGroupDefinition, ruleDefinitions);
    }

    public static RuleDefinitionContext getInstance() {
        return Inner.INSTANCE;
    }

    private static class Inner {
        private static final RuleDefinitionContext INSTANCE = new RuleDefinitionContext();
    }
}
