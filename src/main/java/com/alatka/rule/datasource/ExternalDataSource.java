package com.alatka.rule.datasource;

import com.alatka.rule.context.RuleDataSourceDefinition;

import java.util.Map;

/**
 * 外部数据源
 *
 * @author whocares
 */
public interface ExternalDataSource {

    /**
     * 外部数据源类型
     *
     * @return {@link RuleDataSourceDefinition.Type}
     */
    RuleDataSourceDefinition.Type type();

    /**
     * 使用外部数据源构建请求数据Context
     *
     * @param definition   {@link RuleDataSourceDefinition}
     * @param paramContext 请求数据
     * @return 整合后的请求数据
     */
    Map<String, Object> buildContext(RuleDataSourceDefinition definition, Map<String, Object> paramContext);
}
