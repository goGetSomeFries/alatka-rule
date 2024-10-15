package com.alatka.rule.datasource;

import com.alatka.rule.context.RuleDataSourceDefinition;

import java.util.Map;

public interface DataSourceBuilder {

    RuleDataSourceDefinition.Type getType();

    Map<String, Object> getContext(RuleDataSourceDefinition definition, Map<String, Object> params);
}
