package com.alatka.rule.admin.support;

import com.alatka.rule.core.context.RuleGroupDefinition;
import com.alatka.rule.core.definition.DatabaseRuleDefinitionBuilder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 规则本地测试使用，只加载选中的规则
 */
public class LocalTestingRuleDefinitionBuilder extends DatabaseRuleDefinitionBuilder {

    private Long ruleId;

    private DataSource dataSource;

    public LocalTestingRuleDefinitionBuilder(DataSource dataSource, Long ruleId) {
        super(dataSource);
        this.dataSource = dataSource;
        this.ruleId = ruleId;
    }

    /**
     * 规则本地测试不使用黑/白名单过滤功能
     *
     * @param ruleGroupDefinition 规则组
     * @return
     */
    @Override
    protected Map<String, Object> doBuildRuleListDefinition(RuleGroupDefinition ruleGroupDefinition) {
        return new HashMap<>(0);
    }

    @Override
    protected List<Map<String, Object>> doBuildRuleDefinitions(RuleGroupDefinition ruleGroupDefinition) {
        /*
         * 规则本地测试支持规则、黑/白名单
         */
        String sql = "SELECT * FROM ALK_RULE_DEFINITION WHERE G_KEY = ? AND R_ID = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, ruleGroupDefinition.getId());
            statement.setLong(2, ruleId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new IllegalStateException("No rule exists with id " + ruleId);
                }
                Map<String, Object> result = new HashMap<>();
                result.put("id", resultSet.getString("R_ID"));
                result.put("name", resultSet.getString("R_NAME"));
                result.put("desc", resultSet.getString("R_DESC"));
                result.put("enabled", resultSet.getBoolean("R_ENABLED"));
                result.put("priority", resultSet.getInt("R_PRIORITY"));
                result.put("score", resultSet.getInt("R_SCORE"));
                result.put("order", resultSet.getInt("R_ORDER"));

                return Collections.singletonList(result);
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询ALK_RULE_DEFINITION失败", e);
        }

    }
}
