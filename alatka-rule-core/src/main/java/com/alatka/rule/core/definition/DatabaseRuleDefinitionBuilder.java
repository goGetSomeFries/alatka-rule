package com.alatka.rule.core.definition;

import com.alatka.rule.core.context.RuleGroupDefinition;
import com.alatka.rule.core.context.RuleListDefinition;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 数据库规则构建器
 *
 * @author whocares
 */
public class DatabaseRuleDefinitionBuilder extends AbstractRuleDefinitionBuilder<Map<String, Object>> {

    private final DataSource dataSource;

    private List<Map<String, Object>> ruleUnitList;

    private List<Map<String, Object>> ruleExtendedList;

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
                    result.put("id", resultSet.getString("G_KEY"));
                    result.put("type", resultSet.getString("G_TYPE"));
                    result.put("name", resultSet.getString("G_NAME"));
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
    protected void doPreProcess(Map<String, Object> source) {
        this.ruleUnitList = this.getRuleUnitList(source);
        this.ruleExtendedList = this.getRuleExtendedList(source);
    }

    private List<Map<String, Object>> getRuleUnitList(Map<String, Object> source) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "select * from ALK_RULE_UNIT_DEFINITION WHERE G_KEY = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, source.get("id").toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("ruleId", resultSet.getInt("R_ID"));
                    result.put("dataSource", resultSet.getString("D_KEY"));
                    result.put("expression", resultSet.getString("U_EXPRESSION"));
                    result.put("enabled", resultSet.getBoolean("U_ENABLED"));
                    result.put("order", resultSet.getInt("U_ORDER"));
                    list.add(result);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询ALK_RULE_UNIT_DEFINITION失败", e);
        }
        return list;
    }

    private List<Map<String, Object>> getRuleExtendedList(Map<String, Object> source) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "select * from ALK_RULE_EXTENDED_DEFINITION WHERE G_KEY = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, source.get("id").toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("ruleId", resultSet.getInt("R_ID"));
                    result.put("key", resultSet.getString("E_KEY"));
                    result.put("value", resultSet.getString("E_VALUE"));
                    list.add(result);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询ALK_RULE_EXTENDED_DEFINITION失败", e);
        }
        return list;
    }

    @Override
    protected Map<String, Object> doBuildRuleGroupDefinition(Map<String, Object> source) {
        return source;
    }

    @Override
    protected List<Map<String, Object>> doBuildRuleDataSourceDefinitions(RuleGroupDefinition ruleGroupDefinition) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "select * from ALK_RULE_DATASOURCE_DEFINITION WHERE G_KEY = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, ruleGroupDefinition.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("id", resultSet.getString("D_KEY"));
                    result.put("name", resultSet.getString("D_NAME"));
                    result.put("type", resultSet.getString("D_TYPE"));
                    result.put("enabled", resultSet.getBoolean("D_ENABLED"));
                    result.put("scope", resultSet.getString("D_SCOPE"));
                    result.put("key1", resultSet.getString("D_PARAM_K1"));
                    result.put("value1", resultSet.getString("D_PARAM_V1"));
                    result.put("key2", resultSet.getString("D_PARAM_K2"));
                    result.put("value2", resultSet.getString("D_PARAM_V2"));
                    result.put("key3", resultSet.getString("D_PARAM_K3"));
                    result.put("value3", resultSet.getString("D_PARAM_V3"));
                    result.put("key4", resultSet.getString("D_PARAM_K4"));
                    result.put("value4", resultSet.getString("D_PARAM_V4"));
                    result.put("key5", resultSet.getString("D_PARAM_K5"));
                    result.put("value5", resultSet.getString("D_PARAM_V5"));
                    list.add(result);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询ALK_RULE_DATASOURCE_DEFINITION失败", e);
        }

        list.stream().forEach(map -> {
            Map<String, Object> config = new HashMap<>();
            IntStream.range(1, 6).forEach(i -> {
                String key = this.getValueWithMap(map, "key" + i);
                if (key != null) {
                    config.put(key, this.getValueWithMap(map, "value" + i));
                }
            });
            map.put("config", config);
        });
        return list;
    }

    @Override
    protected List<Map<String, Object>> doBuildRuleParamDefinitions(RuleGroupDefinition ruleGroupDefinition) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "select * from ALK_RULE_PARAM_DEFINITION WHERE G_KEY = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, ruleGroupDefinition.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("id", resultSet.getString("P_KEY"));
                    result.put("name", resultSet.getString("P_NAME"));
                    result.put("enabled", resultSet.getBoolean("P_ENABLED"));
                    result.put("expression", resultSet.getString("P_EXPRESSION"));
                    list.add(result);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询ALK_RULE_PARAM_DEFINITION失败", e);
        }
        return list;
    }

    @Override
    protected Map<String, Object> doBuildRuleListDefinition(RuleGroupDefinition ruleGroupDefinition) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "select * from ALK_RULE_DEFINITION WHERE G_KEY = ? AND R_TYPE IN ('2', '3') ORDER BY R_ORDER";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, ruleGroupDefinition.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("id", resultSet.getString("R_ID"));
                    result.put("type", resultSet.getString("R_TYPE"));
                    result.put("name", resultSet.getString("R_NAME"));
                    result.put("enabled", resultSet.getBoolean("R_ENABLED"));
                    list.add(result);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询ALK_RULE_DEFINITION失败", e);
        }
        Map<String, Object> result = list.isEmpty() ? new HashMap<>(0) : list.get(0);
        result.computeIfPresent("type", (k, v) -> "2".equals(v) ?
                RuleListDefinition.Type.blackList.name() : RuleListDefinition.Type.whiteList.name());
        return result;
    }

    @Override
    protected List<Map<String, Object>> doBuildRuleDefinitions(RuleGroupDefinition ruleGroupDefinition) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "select * from ALK_RULE_DEFINITION WHERE G_KEY = ? AND R_TYPE = '1'";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, ruleGroupDefinition.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("id", resultSet.getString("R_ID"));
                    result.put("name", resultSet.getString("R_NAME"));
                    result.put("desc", resultSet.getString("R_DESC"));
                    result.put("enabled", resultSet.getBoolean("R_ENABLED"));
                    result.put("priority", resultSet.getInt("R_PRIORITY"));
                    result.put("score", resultSet.getInt("R_SCORE"));
                    result.put("order", resultSet.getInt("R_ORDER"));
                    list.add(result);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询ALK_RULE_DEFINITION失败", e);
        }
        return list.stream()
                .sorted(Comparator.comparingInt(map -> this.getValueWithMap(map, "order")))
                .collect(Collectors.toList());
    }

    @Override
    protected Map<String, Object> buildRuleExtendedProperties(Map<String, Object> map) {
        String ruleId = this.getValueWithMap(map, "id");
        return this.ruleExtendedList.stream()
                .filter(mapping -> {
                    int value = this.getValueWithMap(mapping, "ruleId");
                    return ruleId.equals(String.valueOf(value));
                })
                .collect(Collectors.toMap(e -> getValueWithMapOrThrow(e, "key"),
                        e -> getValueWithMapOrThrow(e, "value")));
    }

    @Override
    protected List<Map<String, Object>> doBuildRuleUnitDefinitions(Map<String, Object> map) {
        String ruleId = this.getValueWithMap(map, "id");

        return this.ruleUnitList.stream()
                .filter(mapping -> {
                    int value = this.getValueWithMap(mapping, "ruleId");
                    return ruleId.equals(String.valueOf(value));
                })
                .sorted(Comparator.comparingInt(mapping -> this.getValueWithMap(mapping, "order")))
                .collect(Collectors.toList());
    }

    @Override
    protected void postProcess() {
        // 释放对象
        this.ruleUnitList = null;
        this.ruleExtendedList = null;
    }

}
