package com.alatka.rule.datasource;

import com.alatka.rule.context.RuleDataSourceDefinition;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 数据库外部数据源
 *
 * @author whocares
 */
public class DatabaseExternalDataSource extends AbstractExternalDataSource {

    private static final String KEY_SQL = "sql";

    private static final String KEY_RESULT_TYPE = "resultType";

    private NamedParameterJdbcTemplate jdbcTemplate;

    public DatabaseExternalDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public DatabaseExternalDataSource(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    protected Object doBuildContext(Map<String, String> config, Map<String, Object> paramContext) {
        String sql = this.getWithConfig(config, KEY_SQL);
        String resultType = this.getWithConfig(config, KEY_RESULT_TYPE);

        return ResultType.valueOf(resultType) == ResultType.list ?
                this.jdbcTemplate.queryForList(sql, new MapSqlParameterSource(paramContext)) :
                this.jdbcTemplate.queryForMap(sql, new MapSqlParameterSource(paramContext));
    }

    @Override
    public RuleDataSourceDefinition.Type type() {
        return RuleDataSourceDefinition.Type.database;
    }

    private enum ResultType {
        /**
         * 单笔
         */
        single,
        /**
         * 集合
         */
        list
    }

}
