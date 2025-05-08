package com.alatka.rule.core.datasource;

import com.alatka.rule.core.context.RuleDataSourceDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * 外部数据源抽象类
 *
 * @author whocares
 */
public abstract class AbstractExternalDataSource implements ExternalDataSource {

    @Override
    public Map<String, Object> buildContext(RuleDataSourceDefinition definition, Map<String, Object> paramContext,
                                            Map<String, Object> globalScopeData) {
        // 外部数据源数据范围判定
        Map<String, Object> context = definition.getScope() == RuleDataSourceDefinition.Scope.rule ?
                new HashMap<>(paramContext) : paramContext;

        context.computeIfAbsent(definition.getId(),
                key -> definition.getScope() == RuleDataSourceDefinition.Scope.global ?
                        globalScopeData.computeIfAbsent(key, k -> this.doBuildContext(definition.getConfig(), context)) :
                        this.doBuildContext(definition.getConfig(), context));
        return context;
    }

    /**
     * 构建外部数据源返回的数据，如果结果为null，则不整合到请求数据Context中
     *
     * @param config       {@link RuleDataSourceDefinition#getConfig()}
     * @param paramContext 请求数据
     * @return 外部数据源返回的数据对象
     */
    protected abstract Object doBuildContext(Map<String, String> config, Map<String, Object> paramContext);

    protected String getWithConfig(Map<String, String> config, String key) {
        String value = config.get(key);
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(key + " is required");
        }
        return value;
    }
}
