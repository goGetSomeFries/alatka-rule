package com.alatka.rule.datasource;

import com.alatka.rule.context.RuleDataSourceDefinition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
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
        Mockito.when(jdbcTemplate.queryForList(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class))).thenReturn(Collections.EMPTY_LIST);
        Mockito.when(jdbcTemplate.queryForMap(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class))).thenReturn(Collections.EMPTY_MAP);
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
        Assertions.assertInstanceOf(List.class, externalDataSource.doBuildContext(config, null));
        config.put("resultType", "single");
        Assertions.assertInstanceOf(Map.class, externalDataSource.doBuildContext(config, null));
    }
}
