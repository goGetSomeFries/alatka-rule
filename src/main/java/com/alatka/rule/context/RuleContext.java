package com.alatka.rule.context;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class RuleContext {

    private ConcurrentHashMap<String, List<RuleDefinition>> ruleGroupMap = new ConcurrentHashMap<>();

    public List<RuleDefinition> getRuleGroup(String groupName) {
        return this.ruleGroupMap.get(groupName);
    }

    public static RuleContext getInstance() {
        return Inner.INSTANCE;
    }

    private static class Inner {
        private static final RuleContext INSTANCE = new RuleContext();
    }
}
