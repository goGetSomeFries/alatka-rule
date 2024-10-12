package com.alatka.rule.definition;

import com.alatka.rule.context.RuleDefinition;
import com.alatka.rule.context.RuleGroupDefinition;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class DatabaseRuleDefinitionBuilder extends AbstractRuleDefinitionBuilder<Map<String, Object>> {

    private final DataSource dataSource;

    private List<Map<String, Object>> ruleUnitList;

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
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "select * from ALK_RULE_UNIT_DEFINITION WHERE G_ID = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Integer.parseInt(source.get("id").toString()));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("ruleId", resultSet.getString("R_ID"));
                    result.put("type", resultSet.getString("U_TYPE"));
                    result.put("expression", resultSet.getString("U_EXPRESSION"));
                    result.put("enabled", resultSet.getBoolean("U_ENABLED"));
                    result.put("order", resultSet.getInt("U_ORDER"));
                    list.add(result);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询ALK_RULE_UNIT_DEFINITION失败", e);
        }
        this.ruleUnitList = list;
    }

    @Override
    protected void postProcess() {
        this.ruleUnitList = null;
    }

    @Override
    protected Map<String, Object> doBuildRuleGroupDefinition(Map<String, Object> source) {
        return source;
    }

    @Override
    protected List<Map<String, Object>> doBuildRuleDefinitions(RuleGroupDefinition ruleGroupDefinition) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "select * from ALK_RULE_DEFINITION WHERE G_ID = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Integer.parseInt(ruleGroupDefinition.getId()));
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

    @Override
    protected List<Map<String, Object>> doBuildRuleUnitDefinitions(RuleDefinition ruleDefinition) {
        return this.ruleUnitList.stream()
                .filter(map -> ruleDefinition.getId().equals(this.getValueWithMap(map, "ruleId")))
                .sorted(Comparator.comparingInt(map -> this.getValueWithMap(map, "order")))
                .collect(Collectors.toList());
    }
}
