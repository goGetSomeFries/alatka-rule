package com.alatka.rule.datasource;

import com.alatka.rule.context.RuleDataSourceDefinition;

import java.util.Map;

public class DefaultDataSourceBuilder extends AbstractDataSourceBuilder {

    @Override
    protected Object doBuildContext(RuleDataSourceDefinition definition, Map<String, Object> paramContext) {
        return null;
    }

    @Override
    public RuleDataSourceDefinition.Type type() {
        return RuleDataSourceDefinition.Type.current;
    }
}
