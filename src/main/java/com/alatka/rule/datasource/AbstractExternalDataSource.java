package com.alatka.rule.datasource;

import com.alatka.rule.context.RuleDataSourceDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * 外部数据源抽象类
 *
 * @author whocares
 */
public abstract class AbstractExternalDataSource implements ExternalDataSource {

    @Override
    public Map<String, Object> buildContext(RuleDataSourceDefinition definition, Map<String, Object> paramContext) {
        // TODO
        Map<String, Object> context = definition.getScope() == RuleDataSourceDefinition.Scope.rule ?
                new HashMap<>(paramContext) : paramContext;

        context.computeIfAbsent(definition.getId(), k -> this.doBuildContext(definition, context));
        return context;
    }

    /**
     * 构建外部数据源返回的数据，如果结果为null，则不整合到请求数据Context中
     *
     * @param definition   {@link RuleDataSourceDefinition}
     * @param paramContext 请求数据
     * @return 外部数据源返回的数据对象
     */
    protected abstract Object doBuildContext(RuleDataSourceDefinition definition, Map<String, Object> paramContext);
}
