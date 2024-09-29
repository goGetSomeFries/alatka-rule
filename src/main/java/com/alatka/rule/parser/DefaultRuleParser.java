package com.alatka.rule.parser;

import java.util.Map;

public class DefaultRuleParser extends AbstractRuleParser {

    @Override
    public Map<String, Object> getEnv(Map<String, Object> env) {
        return env;
    }
}
