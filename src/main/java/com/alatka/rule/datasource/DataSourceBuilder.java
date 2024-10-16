package com.alatka.rule.datasource;

import com.alatka.rule.context.RuleDataSourceDefinition;

import java.util.Map;

public interface DataSourceBuilder {

    RuleDataSourceDefinition.Type type();

    Map<String, Object> buildContext(RuleDataSourceDefinition definition, Map<String, Object> paramContext);
}
