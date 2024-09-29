package com.alatka.rule.parser;

import java.util.Collections;
import java.util.Map;

public class DatabaseRuleParser extends AbstractRuleParser {

    private String sql;

    @Override
    protected Map<String, Object> getEnv(Map<String, Object> env) {
        return Collections.emptyMap();
    }
}
