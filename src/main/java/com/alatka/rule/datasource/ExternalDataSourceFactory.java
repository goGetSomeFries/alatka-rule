package com.alatka.rule.datasource;

import com.alatka.rule.context.RuleDataSourceDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * 外部数据源工厂，装配各类{@link ExternalDataSource}实例
 *
 * @author whocares
 */
public class ExternalDataSourceFactory {

    private Map<RuleDataSourceDefinition.Type, ExternalDataSource> externalDataSourceMap = new HashMap<>();

    private ExternalDataSourceFactory() {
        this.init(new DefaultExternalDataSource());
    }

    public void init(ExternalDataSource externalDataSource) {
        if (this.externalDataSourceMap.containsKey(externalDataSource.type())) {
            throw new IllegalArgumentException("DataSourceBuilder already exists");
        }
        this.externalDataSourceMap.put(externalDataSource.type(), externalDataSource);
    }

    public ExternalDataSource getExternalDataSource(RuleDataSourceDefinition.Type type) {
        ExternalDataSource externalDataSource = this.externalDataSourceMap.get(type);
        if (externalDataSource == null) {
            throw new IllegalArgumentException("No DataSourceBuilder found for type " + type);
        }
        return externalDataSource;
    }

    public static ExternalDataSourceFactory getInstance() {
        return ExternalDataSourceFactory.Inner.INSTANCE;
    }

    private static class Inner {
        private static final ExternalDataSourceFactory INSTANCE = new ExternalDataSourceFactory();
    }
}
