package com.alatka.rule.datasource;

import com.alatka.rule.context.RuleDataSourceDefinition;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDataSourceBuilder implements DataSourceBuilder {

    @Override
    public Map<String, Object> buildContext(RuleDataSourceDefinition definition, Map<String, Object> paramContext) {
        Map<String, Object> context = definition.getScope() == RuleDataSourceDefinition.Scope.rule ?
                new HashMap<>(paramContext) : paramContext;
        context.computeIfAbsent(definition.getId(), k -> this.doBuildContext(definition, context));
        return context;
    }

    protected abstract Object doBuildContext(RuleDataSourceDefinition definition, Map<String, Object> paramContext);
}
