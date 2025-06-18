package com.alatka.rule.core.definition;

import com.alatka.rule.core.context.RuleGroupDefinition;
import com.alatka.rule.core.context.RuleListDefinition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.ReflectionMemberAccessor;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.doReturn;

public class DatabaseRuleDefinitionBuilderTest {

    @Test
    @DisplayName("postProcess()")
    void test01() throws NoSuchFieldException, IllegalAccessException {
        DataSource dataSource = Mockito.mock(DataSource.class);
        DatabaseRuleDefinitionBuilder builder = new DatabaseRuleDefinitionBuilder(dataSource);
        ReflectionMemberAccessor reflectionMemberAccessor = new ReflectionMemberAccessor();
        Field field1 = DatabaseRuleDefinitionBuilder.class.getDeclaredField("ruleUnitList");
        reflectionMemberAccessor.set(field1, builder, Collections.EMPTY_LIST);

        Field field2 = DatabaseRuleDefinitionBuilder.class.getDeclaredField("ruleExtendedList");
        reflectionMemberAccessor.set(field2, builder, Collections.EMPTY_LIST);

        Assertions.assertNotNull(reflectionMemberAccessor.get(field1, builder));
        Assertions.assertNotNull(reflectionMemberAccessor.get(field2, builder));

        builder.postProcess();
        Assertions.assertNull(reflectionMemberAccessor.get(field1, builder));
        Assertions.assertNull(reflectionMemberAccessor.get(field2, builder));
    }

    @Disabled
    @Test
    @DisplayName("preProcess()")
    void test02() {
        // TODO
    }

    @Test
    @DisplayName("doBuildRuleGroupDefinition()")
    void test03() {
        DataSource dataSource = Mockito.mock(DataSource.class);
        DatabaseRuleDefinitionBuilder builder = new DatabaseRuleDefinitionBuilder(dataSource);
        HashMap<String, Object> source = new HashMap<>(0);
        Assertions.assertSame(source, builder.doBuildRuleGroupDefinition(source));
    }

    @Test
    @DisplayName("getSources()")
    void test04() throws SQLException {
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        doReturn(true, true, false).when(resultSet).next();
        doReturn("riskAnalysis", "antiFrand").when(resultSet).getString("G_KEY");
        doReturn("all", "once").when(resultSet).getString("G_TYPE");
        doReturn("风控", "反欺诈").when(resultSet).getString("G_NAME");
        doReturn(true, false).when(resultSet).getBoolean("G_ENABLED");

        PreparedStatement statement = Mockito.mock(PreparedStatement.class);
        doReturn(resultSet).when(statement).executeQuery();
        Connection connection = Mockito.mock(Connection.class);
        doReturn(statement).when(connection).prepareStatement("SELECT * FROM ALK_RULE_GROUP_DEFINITION");
        DataSource dataSource = Mockito.mock(DataSource.class);
        doReturn(connection).when(dataSource).getConnection();

        DatabaseRuleDefinitionBuilder builder = new DatabaseRuleDefinitionBuilder(dataSource);
        List<Map<String, Object>> sources = builder.getSources();
        Assertions.assertEquals("riskAnalysis", sources.get(0).get("id"));
        Assertions.assertEquals("antiFrand", sources.get(1).get("id"));
        Assertions.assertEquals("all", sources.get(0).get("type"));
        Assertions.assertEquals("once", sources.get(1).get("type"));
        Assertions.assertEquals("风控", sources.get(0).get("name"));
        Assertions.assertEquals("反欺诈", sources.get(1).get("name"));
        Assertions.assertEquals(true, sources.get(0).get("enabled"));
        Assertions.assertEquals(false, sources.get(1).get("enabled"));
    }

    @Test
    @DisplayName("doBuildRuleParamDefinitions()")
    void test05() throws SQLException {
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        doReturn(true, true, false).when(resultSet).next();
        doReturn("v_amount1", "v_phone_belong").when(resultSet).getString("P_KEY");
        doReturn("v_amount * 100", "string.substring(v_phone, 7)").when(resultSet).getString("P_EXPRESSION");
        doReturn("金额1", "手机号归属地").when(resultSet).getString("P_NAME");
        doReturn(true, false).when(resultSet).getBoolean("P_ENABLED");

        PreparedStatement statement = Mockito.mock(PreparedStatement.class);
        doReturn(resultSet).when(statement).executeQuery();
        Connection connection = Mockito.mock(Connection.class);
        doReturn(statement).when(connection).prepareStatement("SELECT * FROM ALK_RULE_PARAM_DEFINITION WHERE G_KEY = ?");
        DataSource dataSource = Mockito.mock(DataSource.class);
        doReturn(connection).when(dataSource).getConnection();

        DatabaseRuleDefinitionBuilder builder = new DatabaseRuleDefinitionBuilder(dataSource);
        List<Map<String, Object>> sources = builder.doBuildRuleParamDefinitions(new RuleGroupDefinition());
        Assertions.assertEquals("v_amount1", sources.get(0).get("id"));
        Assertions.assertEquals("v_phone_belong", sources.get(1).get("id"));
        Assertions.assertEquals("v_amount * 100", sources.get(0).get("expression"));
        Assertions.assertEquals("string.substring(v_phone, 7)", sources.get(1).get("expression"));
        Assertions.assertEquals("金额1", sources.get(0).get("name"));
        Assertions.assertEquals("手机号归属地", sources.get(1).get("name"));
        Assertions.assertEquals(true, sources.get(0).get("enabled"));
        Assertions.assertEquals(false, sources.get(1).get("enabled"));
    }

    @Test
    @DisplayName("doBuildRuleDefinitions()")
    void test06() throws SQLException {
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        doReturn(true, true, false).when(resultSet).next();
        doReturn("1", "2").when(resultSet).getString("R_ID");
        doReturn("欺诈9星", "欺诈8星").when(resultSet).getString("R_NAME");
        doReturn("testing1", "testing2").when(resultSet).getString("R_DESC");
        doReturn(true, false).when(resultSet).getBoolean("R_ENABLED");
        doReturn(1, 2).when(resultSet).getInt("R_PRIORITY");
        doReturn(1, 2).when(resultSet).getInt("R_SCORE");
        doReturn(2, 1).when(resultSet).getInt("R_ORDER");

        PreparedStatement statement = Mockito.mock(PreparedStatement.class);
        doReturn(resultSet).when(statement).executeQuery();
        Connection connection = Mockito.mock(Connection.class);
        doReturn(statement).when(connection).prepareStatement("SELECT * FROM ALK_RULE_DEFINITION WHERE G_KEY = ? AND R_TYPE = '1'");
        DataSource dataSource = Mockito.mock(DataSource.class);
        doReturn(connection).when(dataSource).getConnection();

        DatabaseRuleDefinitionBuilder builder = new DatabaseRuleDefinitionBuilder(dataSource);
        List<Map<String, Object>> sources = builder.doBuildRuleDefinitions(new RuleGroupDefinition());
        Assertions.assertEquals("2", sources.get(0).get("id"));
        Assertions.assertEquals("1", sources.get(1).get("id"));
        Assertions.assertEquals("欺诈8星", sources.get(0).get("name"));
        Assertions.assertEquals("欺诈9星", sources.get(1).get("name"));
        Assertions.assertEquals("testing2", sources.get(0).get("desc"));
        Assertions.assertEquals("testing1", sources.get(1).get("desc"));
        Assertions.assertEquals(false, sources.get(0).get("enabled"));
        Assertions.assertEquals(true, sources.get(1).get("enabled"));
        Assertions.assertEquals(2, sources.get(0).get("priority"));
        Assertions.assertEquals(1, sources.get(1).get("priority"));
        Assertions.assertEquals(2, sources.get(0).get("score"));
        Assertions.assertEquals(1, sources.get(1).get("score"));
        Assertions.assertEquals(1, sources.get(0).get("order"));
        Assertions.assertEquals(2, sources.get(1).get("order"));
    }

    @Test
    @DisplayName("doBuildRuleListDefinition()")
    void test07() throws SQLException {
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        doReturn(true, true, false).when(resultSet).next();
        doReturn("1", "2").when(resultSet).getString("R_ID");
        doReturn("白名单", "黑名单").when(resultSet).getString("R_NAME");
        doReturn("3", "2").when(resultSet).getString("R_TYPE");
        doReturn(true, false).when(resultSet).getBoolean("R_ENABLED");

        PreparedStatement statement = Mockito.mock(PreparedStatement.class);
        doReturn(resultSet).when(statement).executeQuery();
        Connection connection = Mockito.mock(Connection.class);
        doReturn(statement).when(connection).prepareStatement("SELECT * FROM ALK_RULE_DEFINITION WHERE G_KEY = ? AND R_TYPE IN ('2', '3') ORDER BY R_ORDER");
        DataSource dataSource = Mockito.mock(DataSource.class);
        doReturn(connection).when(dataSource).getConnection();

        DatabaseRuleDefinitionBuilder builder = new DatabaseRuleDefinitionBuilder(dataSource);
        Map<String, Object> sources = builder.doBuildRuleListDefinition(new RuleGroupDefinition());
        Assertions.assertEquals("1", sources.get("id"));
        Assertions.assertEquals("白名单", sources.get("name"));
        Assertions.assertEquals(RuleListDefinition.Type.whiteList.name(), sources.get("type"));
        Assertions.assertEquals(true, sources.get("enabled"));
    }

    @Test
    @DisplayName("doBuildRuleListDefinition() 无数据")
    void test08() throws SQLException {
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        doReturn(false).when(resultSet).next();

        PreparedStatement statement = Mockito.mock(PreparedStatement.class);
        doReturn(resultSet).when(statement).executeQuery();
        Connection connection = Mockito.mock(Connection.class);
        doReturn(statement).when(connection).prepareStatement("SELECT * FROM ALK_RULE_DEFINITION WHERE G_KEY = ? AND R_TYPE IN ('2', '3') ORDER BY R_ORDER");
        DataSource dataSource = Mockito.mock(DataSource.class);
        doReturn(connection).when(dataSource).getConnection();

        DatabaseRuleDefinitionBuilder builder = new DatabaseRuleDefinitionBuilder(dataSource);
        Map<String, Object> sources = builder.doBuildRuleListDefinition(new RuleGroupDefinition());
        Assertions.assertTrue(sources.isEmpty());
    }

    @Test
    @DisplayName("doBuildRuleUnitDefinitions()")
    void test09() throws SQLException, NoSuchFieldException, IllegalAccessException {
        Map<String, Object> map = Mockito.mock(Map.class);
        Mockito.doReturn(1, 1, 2, 3, 4, 1).when(map).get("ruleId");
        Mockito.doReturn(1, 3, 1, 1, 1, 2).when(map).get("order");
        Mockito.doReturn("key1", "key2", "key3", "key4", "key5", "key6").when(map).get("key");
        Map<String, Object> map1 = new HashMap<>();
        map1.put("ruleId", 1);
        map1.put("order", 1);
        map1.put("key", "key1");
        Map<String, Object> map2 = new HashMap<>();
        map2.put("ruleId", 1);
        map2.put("order", 3);
        map2.put("key", "key2");
        Map<String, Object> map3 = new HashMap<>();
        map3.put("ruleId", 2);
        map3.put("order", 1);
        map3.put("key", "key3");
        Map<String, Object> map4 = new HashMap<>();
        map4.put("ruleId", 3);
        map4.put("order", 1);
        map4.put("key", "key4");
        Map<String, Object> map5 = new HashMap<>();
        map5.put("ruleId", 1);
        map5.put("order", 2);
        map5.put("key", "key5");
        List<Map<String, Object>> list = Stream.of(map1, map2, map3, map4, map5).collect(Collectors.toList());

        DataSource dataSource = Mockito.mock(DataSource.class);
        DatabaseRuleDefinitionBuilder builder = new DatabaseRuleDefinitionBuilder(dataSource);
        ReflectionMemberAccessor reflectionMemberAccessor = new ReflectionMemberAccessor();
        Field field = DatabaseRuleDefinitionBuilder.class.getDeclaredField("ruleUnitList");
        reflectionMemberAccessor.set(field, builder, list);

        List<Map<String, Object>> result = builder.doBuildRuleUnitDefinitions(Collections.singletonMap("id", "1"));

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals("key1", result.get(0).get("key"));
        Assertions.assertEquals("key5", result.get(1).get("key"));
        Assertions.assertEquals("key2", result.get(2).get("key"));
    }

    @Test
    @Disabled
    @DisplayName("doBuildRuleDataSourceDefinitions()")
    void test10() throws SQLException {
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        doReturn(true, false).when(resultSet).next();
        doReturn("tradeDetail").when(resultSet).getString("D_KEY");
        doReturn("交易明细").when(resultSet).getString("D_NAME");
        doReturn("database").when(resultSet).getString("D_TYPE");
        doReturn(true).when(resultSet).getBoolean("D_ENABLED");
        doReturn("rule").when(resultSet).getString("D_SCOPE");
        doReturn("sql").when(resultSet).getString("D_PARAM_K1");
        doReturn("select * from trade_detail where date = :date").when(resultSet).getString("D_PARAM_V1");

        PreparedStatement statement = Mockito.mock(PreparedStatement.class);
        doReturn(resultSet).when(statement).executeQuery();
        Connection connection = Mockito.mock(Connection.class);
        doReturn(statement).when(connection).prepareStatement("SELECT * FROM ALK_RULE_DATASOURCE_DEFINITION WHERE G_KEY = ?");
        DataSource dataSource = Mockito.mock(DataSource.class);
        doReturn(connection).when(dataSource).getConnection();

        DatabaseRuleDefinitionBuilder builder = new DatabaseRuleDefinitionBuilder(dataSource);
        List<Map<String, Object>> sources = builder.doBuildRuleDataSourceDefinitions(new RuleGroupDefinition());

        Assertions.assertEquals("tradeDetail", sources.get(0).get("id"));
        Assertions.assertEquals("交易明细", sources.get(0).get("name"));
        Assertions.assertEquals("database", sources.get(0).get("type"));
        Assertions.assertEquals(true, sources.get(0).get("enabled"));
        Assertions.assertEquals("rule", sources.get(0).get("scope"));
        Map<String, Object> config = (Map<String, Object>) sources.get(0).get("config");
        Assertions.assertEquals("select * from trade_detail where date = :date", config.get("sql"));
    }

    @Test
    @DisplayName("getRuleUnitList()")
    void test11() throws SQLException, NoSuchMethodException {
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        doReturn(true, true, false).when(resultSet).next();
        doReturn(1, 2).when(resultSet).getInt("R_ID");
        doReturn("riskAnalysis", "antiFrand").when(resultSet).getString("D_KEY");
        doReturn("all", "once").when(resultSet).getString("U_EXPRESSION");
        doReturn(true, false).when(resultSet).getBoolean("U_ENABLED");
        doReturn(1, 2).when(resultSet).getInt("U_ORDER");

        PreparedStatement statement = Mockito.mock(PreparedStatement.class);
        doReturn(resultSet).when(statement).executeQuery();
        Connection connection = Mockito.mock(Connection.class);
        doReturn(statement).when(connection).prepareStatement("SELECT * FROM ALK_RULE_UNIT_DEFINITION WHERE G_KEY = ?");
        DataSource dataSource = Mockito.mock(DataSource.class);
        doReturn(connection).when(dataSource).getConnection();

        DatabaseRuleDefinitionBuilder builder = new DatabaseRuleDefinitionBuilder(dataSource);
        List<Map<String, Object>> result = (List<Map<String, Object>>) ReflectionUtils.invokeMethod(DatabaseRuleDefinitionBuilder.class.getDeclaredMethod("getRuleUnitList", Map.class), builder, Collections.singletonMap("id", "1"));

        Assertions.assertEquals("riskAnalysis", result.get(0).get("dataSource"));
        Assertions.assertEquals("antiFrand", result.get(1).get("dataSource"));
        Assertions.assertEquals("all", result.get(0).get("expression"));
        Assertions.assertEquals("once", result.get(1).get("expression"));
        Assertions.assertEquals(true, result.get(0).get("enabled"));
        Assertions.assertEquals(false, result.get(1).get("enabled"));
        Assertions.assertEquals(1, result.get(0).get("order"));
        Assertions.assertEquals(2, result.get(1).get("order"));
        Assertions.assertEquals(1, result.get(0).get("ruleId"));
        Assertions.assertEquals(2, result.get(1).get("ruleId"));
    }

    @Test
    @DisplayName("getRuleExtendedList()")
    void test12() throws SQLException, NoSuchMethodException {
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        doReturn(true, true, false).when(resultSet).next();
        doReturn(1, 2).when(resultSet).getInt("R_ID");
        doReturn("color", "autoAction").when(resultSet).getString("E_KEY");
        doReturn("#000FFF", "true").when(resultSet).getString("E_VALUE");

        PreparedStatement statement = Mockito.mock(PreparedStatement.class);
        doReturn(resultSet).when(statement).executeQuery();
        Connection connection = Mockito.mock(Connection.class);
        doReturn(statement).when(connection).prepareStatement("SELECT * FROM ALK_RULE_EXT_DEFINITION WHERE G_KEY = ?");
        DataSource dataSource = Mockito.mock(DataSource.class);
        doReturn(connection).when(dataSource).getConnection();

        DatabaseRuleDefinitionBuilder builder = new DatabaseRuleDefinitionBuilder(dataSource);
        List<Map<String, Object>> result = (List<Map<String, Object>>) ReflectionUtils.invokeMethod(DatabaseRuleDefinitionBuilder.class.getDeclaredMethod("getRuleExtendedList", Map.class), builder, Collections.singletonMap("id", "1"));

        Assertions.assertEquals("color", result.get(0).get("key"));
        Assertions.assertEquals("autoAction", result.get(1).get("key"));
        Assertions.assertEquals("#000FFF", result.get(0).get("value"));
        Assertions.assertEquals("true", result.get(1).get("value"));
        Assertions.assertEquals(1, result.get(0).get("ruleId"));
        Assertions.assertEquals(2, result.get(1).get("ruleId"));
    }

    @Test
    @DisplayName("doBuildRuleDataSourceDefinitions()")
    void test13() throws SQLException {
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        doReturn(true, true, false).when(resultSet).next();
        doReturn(1L, 2L).when(resultSet).getLong("D_ID");
        doReturn("tradeDetail", "params").when(resultSet).getString("D_KEY");
        doReturn("近10天交易明细", "系统参数").when(resultSet).getString("D_NAME");
        doReturn("database", "redis").when(resultSet).getString("D_TYPE");
        doReturn(true, false).when(resultSet).getBoolean("D_ENABLED");
        doReturn("request", "global").when(resultSet).getString("D_SCOPE");

        PreparedStatement statement = Mockito.mock(PreparedStatement.class);
        doReturn(resultSet).when(statement).executeQuery();
        Connection connection = Mockito.mock(Connection.class);
        doReturn(statement).when(connection).prepareStatement("SELECT * FROM ALK_RULE_DATASOURCE_DEFINITION WHERE G_KEY = ?");
        DataSource dataSource = Mockito.mock(DataSource.class);
        doReturn(connection).when(dataSource).getConnection();

        ResultSet resultSet1 = Mockito.mock(ResultSet.class);
        doReturn(true, true, true, false).when(resultSet1).next();
        doReturn(1L, 1L, 1L).when(resultSet1).getLong("D_ID");
        doReturn("resultType", "resultClass", "sql").when(resultSet1).getString("S_KEY");
        doReturn("list", "com.example.TradeDetailEntity", "select * from trade_detail where trade_time > sysdate - 10").when(resultSet1).getString("S_VALUE");

        PreparedStatement statement1 = Mockito.mock(PreparedStatement.class);
        doReturn(resultSet1).when(statement1).executeQuery();
        doReturn(statement1).when(connection).prepareStatement("SELECT * FROM ALK_RULE_DATASOURCE_EXT_DEFINITION WHERE G_KEY = ?");

        DatabaseRuleDefinitionBuilder builder = new DatabaseRuleDefinitionBuilder(dataSource);
        RuleGroupDefinition ruleGroupDefinition = new RuleGroupDefinition();
        ruleGroupDefinition.setId("1");
        List<Map<String, Object>> result = builder.doBuildRuleDataSourceDefinitions(ruleGroupDefinition);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(1L, result.get(0).get("identity"));
        Assertions.assertEquals(2L, result.get(1).get("identity"));
        Assertions.assertEquals("tradeDetail", result.get(0).get("id"));
        Assertions.assertEquals("params", result.get(1).get("id"));
        Assertions.assertEquals("近10天交易明细", result.get(0).get("name"));
        Assertions.assertEquals("系统参数", result.get(1).get("name"));
        Assertions.assertEquals("database", result.get(0).get("type"));
        Assertions.assertEquals("redis", result.get(1).get("type"));
        Assertions.assertEquals(true, result.get(0).get("enabled"));
        Assertions.assertEquals(false, result.get(1).get("enabled"));
        Assertions.assertEquals("request", result.get(0).get("scope"));
        Assertions.assertEquals("global", result.get(1).get("scope"));
        Map<String, Object> config = (Map<String, Object>) result.get(0).get("config");
        Assertions.assertEquals("com.example.TradeDetailEntity", config.get("resultClass"));
        Assertions.assertEquals("list", config.get("resultType"));
        Assertions.assertEquals("select * from trade_detail where trade_time > sysdate - 10", config.get("sql"));
        Assertions.assertNull(result.get(1).get("config"));
    }

    @Test
    @DisplayName("buildRuleExtendedProperties()")
    void test14() throws NoSuchFieldException, IllegalAccessException {
        DataSource dataSource = Mockito.spy(DataSource.class);
        DatabaseRuleDefinitionBuilder builder = new DatabaseRuleDefinitionBuilder(dataSource);
        ReflectionMemberAccessor reflectionMemberAccessor = new ReflectionMemberAccessor();

        List<Map<String, Object>> ruleExtendedList = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("ruleId", 1);
        map1.put("key", "color");
        map1.put("value", "#FFFFFF");
        Map<String, Object> map2 = new HashMap<>();
        map2.put("ruleId", 1);
        map2.put("key", "action");
        map2.put("value", "sms");
        Map<String, Object> map3 = new HashMap<>();
        map3.put("ruleId", 2);
        map3.put("key", "color");
        map3.put("value", "#000000");
        ruleExtendedList.add(map1);
        ruleExtendedList.add(map2);
        ruleExtendedList.add(map3);
        reflectionMemberAccessor.set(DatabaseRuleDefinitionBuilder.class.getDeclaredField("ruleExtendedList"), builder, ruleExtendedList);

        Map<String, Object> result = builder.buildRuleExtendedProperties(Collections.singletonMap("id", "1"));
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("#FFFFFF", result.get("color"));
        Assertions.assertEquals("sms", result.get("action"));
    }

}
