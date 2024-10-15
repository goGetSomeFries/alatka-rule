package com.alatka.rule.datasource;

import com.alatka.rule.context.RuleDataSourceDefinition;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDataSourceBuilder implements DataSourceBuilder {

    @Override
    public Map<String, Object> getContext(RuleDataSourceDefinition definition, Map<String, Object> params) {
        Map<String, Object> p = definition.getScope() == RuleDataSourceDefinition.Scope.rule ?
                new HashMap<>(params) : params;
        if (!params.containsKey(definition.getId())) {
            Object result = this.doGetContext(params);
            p.put(definition.getId(), result);
        }
        return p;
    }

    protected abstract <T> T doGetContext(Map<String, Object> params);
}
