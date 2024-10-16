package com.alatka.rule.datasource;

import com.alatka.rule.context.RuleDataSourceDefinition;

import java.util.HashMap;
import java.util.Map;

public class DataSourceBuilderFactory {

    private Map<RuleDataSourceDefinition.Type, DataSourceBuilder> dataSourceBuilders = new HashMap<>();

    private DataSourceBuilderFactory() {
        this.init(new DefaultDataSourceBuilder());
    }

    public void init(DataSourceBuilder builder) {
        if (this.dataSourceBuilders.containsKey(builder.type())) {
            throw new IllegalArgumentException("DataSourceBuilder already exists");
        }
        this.dataSourceBuilders.put(builder.type(), builder);
    }

    public DataSourceBuilder getDataSourceBuilder(RuleDataSourceDefinition.Type type) {
        DataSourceBuilder dataSourceBuilder = this.dataSourceBuilders.get(type);
        if (dataSourceBuilder == null) {
            throw new IllegalArgumentException("No DataSourceBuilder found for type " + type);
        }
        return dataSourceBuilder;
    }

    public static DataSourceBuilderFactory getInstance() {
        return DataSourceBuilderFactory.Inner.INSTANCE;
    }

    private static class Inner {
        private static final DataSourceBuilderFactory INSTANCE = new DataSourceBuilderFactory();
    }
}
