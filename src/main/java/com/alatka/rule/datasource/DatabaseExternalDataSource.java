package com.alatka.rule.datasource;

import com.alatka.rule.context.RuleDataSourceDefinition;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.Map;

public class DatabaseExternalDataSource extends AbstractExternalDataSource {

    private static final String KEY_SQL = "sql";

    private NamedParameterJdbcTemplate jdbcTemplate;

    public DatabaseExternalDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    protected Object doBuildContext(RuleDataSourceDefinition definition, Map<String, Object> paramContext) {
        Map<String, Object> config = definition.getConfig();
        String sql = config.get(KEY_SQL).toString();
        return definition.getResultType() == RuleDataSourceDefinition.ResultType.list ?
                this.jdbcTemplate.queryForList(sql, new MapSqlParameterSource(paramContext)) :
                this.jdbcTemplate.queryForMap(sql, new MapSqlParameterSource(paramContext));
    }

    @Override
    public RuleDataSourceDefinition.Type type() {
        return RuleDataSourceDefinition.Type.database;
    }
}
