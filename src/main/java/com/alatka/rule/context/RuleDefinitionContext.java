package com.alatka.rule.context;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class RuleDefinitionContext {

    private ConcurrentHashMap<RuleGroupDefinition, List<RuleDefinition>> ruleGroupMap = new ConcurrentHashMap<>();

    public List<RuleDefinition> getRuleGroup(String groupName) {
        RuleGroupDefinition ruleGroupDefinition = new RuleGroupDefinition(groupName);
        List<RuleDefinition> ruleDefinitions = this.ruleGroupMap.get(ruleGroupDefinition);
        if (ruleDefinitions == null) {
            throw new IllegalArgumentException(groupName + " not exists");
        }
        return ruleDefinitions;
    }

    public void setRuleGroup(RuleGroupDefinition ruleGroupDefinition, List<RuleDefinition> ruleDefinitions) {
        this.ruleGroupMap.put(ruleGroupDefinition, ruleDefinitions);
    }

    public static RuleDefinitionContext getInstance() {
        return Inner.INSTANCE;
    }

    private static class Inner {
        private static final RuleDefinitionContext INSTANCE = new RuleDefinitionContext();
    }
}
