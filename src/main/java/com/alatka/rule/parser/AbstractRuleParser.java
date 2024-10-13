package com.alatka.rule.parser;

import java.util.Map;

public abstract class AbstractRuleParser implements RuleParser {

    @Override
    public Map<String, Object> getEnv(Map<String, Object> env, boolean flag) {
        if (flag) {
            Map<String, Object> other = this.getEnv(env);
            env.putAll(other);
        }
        return env;
    }

    protected abstract Map<String, Object> getEnv(Map<String, Object> env);
}
