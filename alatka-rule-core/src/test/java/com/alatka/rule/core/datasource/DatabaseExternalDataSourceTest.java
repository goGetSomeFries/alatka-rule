package com.alatka.rule.core.datasource;

import com.alatka.rule.core.context.RuleDataSourceDefinition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseExternalDataSourceTest {

    @Test
    @DisplayName("type()")
    void test01() {
        DataSource dataSource = Mockito.spy(DataSource.class);
        ExternalDataSource externalDataSource = new DatabaseExternalDataSource(dataSource);
        Assertions.assertSame(RuleDataSourceDefinition.Type.database, externalDataSource.type());
    }

    @Test
    @DisplayName("doBuildContext()")
    void test02() {
        NamedParameterJdbcTemplate jdbcTemplate = Mockito.mock(NamedParameterJdbcTemplate.class);
        Mockito.when(jdbcTemplate.queryForList(Mockito.anyString(), Mockito.anyMap())).thenReturn(Collections.EMPTY_LIST);
        Mockito.when(jdbcTemplate.queryForList(Mockito.anyString(), Mockito.anyMap(), Mockito.any(Class.class))).thenReturn(Collections.singletonList("123"));
        Mockito.when(jdbcTemplate.queryForMap(Mockito.anyString(), Mockito.anyMap())).thenReturn(Collections.EMPTY_MAP);
        Mockito.when(jdbcTemplate.queryForObject(Mockito.anyString(), Mockito.anyMap(), Mockito.any(Class.class))).thenReturn("123");
        DatabaseExternalDataSource externalDataSource = new DatabaseExternalDataSource(jdbcTemplate);

        Map<String, String> config = new HashMap<>();
        Assertions.assertThrows(IllegalArgumentException.class, () -> externalDataSource.doBuildContext(config, null), "sql is required");

        config.put("sql", "");
        Assertions.assertThrows(IllegalArgumentException.class, () -> externalDataSource.doBuildContext(config, null), "sql is required");

        config.put("sql", "testing");
        Assertions.assertThrows(IllegalArgumentException.class, () -> externalDataSource.doBuildContext(config, null), "resultType is required");

        config.put("resultType", "");
        Assertions.assertThrows(IllegalArgumentException.class, () -> externalDataSource.doBuildContext(config, null), "resultType is required");

        config.put("resultType", "list");
        Assertions.assertInstanceOf(List.class, externalDataSource.doBuildContext(config, Collections.EMPTY_MAP));
        Assertions.assertEquals(0, ((List) externalDataSource.doBuildContext(config, Collections.EMPTY_MAP)).size());
        config.put("resultClass", "java.lang.String");
        Assertions.assertEquals(1, ((List) externalDataSource.doBuildContext(config, Collections.EMPTY_MAP)).size());
        Assertions.assertEquals("123", ((List) externalDataSource.doBuildContext(config, Collections.EMPTY_MAP)).get(0));

        config.put("resultType", "single");
        Assertions.assertEquals("123", externalDataSource.doBuildContext(config, Collections.EMPTY_MAP));
        config.put("resultClass", null);
        Assertions.assertInstanceOf(Map.class, externalDataSource.doBuildContext(config, Collections.EMPTY_MAP));
    }
}
