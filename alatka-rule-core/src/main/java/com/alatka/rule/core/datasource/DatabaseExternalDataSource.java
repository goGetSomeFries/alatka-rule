package com.alatka.rule.core.datasource;

import com.alatka.rule.core.context.RuleDataSourceDefinition;
import com.alatka.rule.core.util.ClassUtil;
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

    private static final String KEY_RESULT_CLASS = "resultClass";

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
        String resultClass = this.getWithConfig(config, KEY_RESULT_CLASS);
        Class<?> clazz = ClassUtil.forName(resultClass);


        if (ResultType.valueOf(resultType) == ResultType.list) {
            return clazz == null ? this.jdbcTemplate.queryForList(sql, paramContext) :
                    this.jdbcTemplate.queryForList(sql, paramContext, clazz);
        }
        return clazz == null ? this.jdbcTemplate.queryForMap(sql, paramContext) :
                this.jdbcTemplate.queryForObject(sql, paramContext, clazz);
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
