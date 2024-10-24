package com.alatka.rule.datasource;

import com.alatka.rule.context.RuleDataSourceDefinition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.ReflectionMemberAccessor;

import javax.sql.DataSource;
import java.util.Map;

public class ExternalDataSourceFactoryTest {

    @Test
    @DisplayName("单例")
    void test01() {
        ExternalDataSourceFactory instance1 = ExternalDataSourceFactory.getInstance();
        ExternalDataSourceFactory instance2 = ExternalDataSourceFactory.getInstance();

        Assertions.assertSame(instance1, instance2);
    }

    @Test
    @DisplayName("init()")
    void test02() {
        ExternalDataSourceFactory instance = ExternalDataSourceFactory.getInstance();
        Assertions.assertThrows(IllegalArgumentException.class, () -> instance.init(new DefaultExternalDataSource()), "No DataSourceBuilder found for type " + RuleDataSourceDefinition.Type.current);
        DataSource dataSource = Mockito.mock(DataSource.class);
        Assertions.assertDoesNotThrow(() -> instance.init(new DatabaseExternalDataSource(dataSource)));
    }

    @Test
    @DisplayName("ExternalDataSourceFactory()")
    void test03() {
        ExternalDataSourceFactory instance = ExternalDataSourceFactory.getInstance();
        ExternalDataSource externalDataSource = instance.getExternalDataSource(RuleDataSourceDefinition.Type.current);
        Assertions.assertInstanceOf(DefaultExternalDataSource.class, externalDataSource);
    }

    @Test
    @DisplayName("getExternalDataSource()")
    void test04() throws IllegalAccessException, NoSuchFieldException {
        Map<RuleDataSourceDefinition.Type, ExternalDataSource> externalDataSourceMap = Mockito.spy(Map.class);
        DefaultExternalDataSource externalDataSource = new DefaultExternalDataSource();
        Mockito.when(externalDataSourceMap.get(Mockito.any())).thenReturn(null).thenReturn(externalDataSource);

        ReflectionMemberAccessor reflectionMemberAccessor = new ReflectionMemberAccessor();
        reflectionMemberAccessor.set(ExternalDataSourceFactory.class.getDeclaredField("externalDataSourceMap"), ExternalDataSourceFactory.getInstance(), externalDataSourceMap);

        Assertions.assertThrows(IllegalArgumentException.class, () -> ExternalDataSourceFactory.getInstance().getExternalDataSource(RuleDataSourceDefinition.Type.database), "No DataSourceBuilder found for type " + RuleDataSourceDefinition.Type.database);
        Assertions.assertSame(externalDataSource, ExternalDataSourceFactory.getInstance().getExternalDataSource(RuleDataSourceDefinition.Type.database));

    }

}
