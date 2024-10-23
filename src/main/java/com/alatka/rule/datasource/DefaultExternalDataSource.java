package com.alatka.rule.datasource;

import com.alatka.rule.context.RuleDataSourceDefinition;

import java.util.Map;

/**
 * 默认外部数据源，使用当前请求数据
 *
 * @author whocares
 */
public class DefaultExternalDataSource extends AbstractExternalDataSource {

    @Override
    protected Object doBuildContext(Map<String, String> config, Map<String, Object> paramContext) {
        return null;
    }

    @Override
    public RuleDataSourceDefinition.Type type() {
        return RuleDataSourceDefinition.Type.current;
    }
}
