package com.alatka.rule.definition;

import com.alatka.rule.context.RuleDefinition;
import com.alatka.rule.context.RuleGroupDefinition;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseRuleDefinitionBuilder extends AbstractRuleDefinitionBuilder<Map<String, Object>> {

    private final DataSource dataSource;

    public DatabaseRuleDefinitionBuilder(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected List<Map<String, Object>> getSources() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "select * from ALK_RULE_GROUP_DEFINITION";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("id", resultSet.getInt("G_ID"));
                    result.put("type", resultSet.getString("G_TYPE"));
                    result.put("desc", resultSet.getString("G_DESC"));
                    result.put("enabled", resultSet.getBoolean("G_ENABLED"));
                    list.add(result);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询ALK_RULE_GROUP_DEFINITION失败", e);
        }
        return list;
    }

    @Override
    protected void preProcess(Map<String, Object> source) {

    }

    @Override
    protected RuleGroupDefinition buildRuleGroupDefinition(Map<String, Object> source) {
        String id = this.getValueWithMap(source, "id");
        String desc = this.getValueWithMap(source, "desc");
        boolean enabled = this.getValueWithMap(source, "enabled");

        RuleGroupDefinition ruleGroupDefinition = new RuleGroupDefinition();
        ruleGroupDefinition.setId(id);
        ruleGroupDefinition.setDesc(desc);
        ruleGroupDefinition.setEnabled(enabled);
        return ruleGroupDefinition;
    }

    @Override
    protected List<RuleDefinition> buildRuleDefinitions(Map<String, Object> source) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "select * from ALK_RULE_DEFINITION WHERE G_ID = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Integer.parseInt(source.get("id").toString()));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("id", resultSet.getInt("R_ID"));
                    result.put("desc", resultSet.getString("R_DESC"));
                    result.put("remark", resultSet.getString("R_REMARK"));
                    result.put("enabled", resultSet.getBoolean("R_ENABLED"));
                    list.add(result);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询ALK_RULE_DEFINITION失败", e);
        }
        return list;
    }
}
