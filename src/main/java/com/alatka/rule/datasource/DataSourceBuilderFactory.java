package com.alatka.rule.datasource;

import com.alatka.rule.context.RuleDataSourceDefinition;

import java.util.HashMap;
import java.util.Map;

public class DataSourceBuilderFactory {

    private Map<RuleDataSourceDefinition.Type, ExternalDataSource> dataSourceBuilders = new HashMap<>();

    private DataSourceBuilderFactory() {
        this.init(new DefaultExternalDataSource());
    }

    public void init(ExternalDataSource builder) {
        if (this.dataSourceBuilders.containsKey(builder.type())) {
            throw new IllegalArgumentException("DataSourceBuilder already exists");
        }
        this.dataSourceBuilders.put(builder.type(), builder);
    }

    public ExternalDataSource getDataSourceBuilder(RuleDataSourceDefinition.Type type) {
        ExternalDataSource externalDataSource = this.dataSourceBuilders.get(type);
        if (externalDataSource == null) {
            throw new IllegalArgumentException("No DataSourceBuilder found for type " + type);
        }
        return externalDataSource;
    }

    public static DataSourceBuilderFactory getInstance() {
        return DataSourceBuilderFactory.Inner.INSTANCE;
    }

    private static class Inner {
        private static final DataSourceBuilderFactory INSTANCE = new DataSourceBuilderFactory();
    }
}
