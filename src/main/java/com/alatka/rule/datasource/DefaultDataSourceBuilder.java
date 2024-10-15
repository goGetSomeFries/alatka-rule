package com.alatka.rule.datasource;

import com.alatka.rule.context.RuleDataSourceDefinition;

import java.util.Map;

public class DefaultDataSourceBuilder extends AbstractDataSourceBuilder {
    @Override
    protected <T> T doGetContext(Map<String, Object> params) {
        return null;
    }

    @Override
    public RuleDataSourceDefinition.Type getType() {
        return null;
    }
}
