package com.alatka.rule.core.definition;

import com.alatka.rule.core.support.FileWrapper;
import com.alatka.rule.core.util.XmlUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.ReflectionMemberAccessor;

import java.util.*;

public class XmlRuleDefinitionBuilderTest {

    @Test
    @DisplayName("suffix()")
    void test01() {
        XmlRuleDefinitionBuilder builder = new XmlRuleDefinitionBuilder();

        Assertions.assertEquals(".xml", builder.suffix()[0]);
    }

    @Test
    @DisplayName("doBuildRuleUnitDefinitions()")
    void test02() {
        XmlRuleDefinitionBuilder builder = new XmlRuleDefinitionBuilder();
        Map<String, Object> map = new HashMap<>();
        Assertions.assertEquals(0, builder.doBuildRuleUnitDefinitions(map).size());

        map.put("unit", Collections.singletonList(Collections.singletonMap("key", "value")));
        Assertions.assertEquals(1, builder.doBuildRuleUnitDefinitions(map).size());

        map.put("unit", Collections.singletonMap("key", "value"));
        Assertions.assertEquals(1, builder.doBuildRuleUnitDefinitions(map).size());
    }

    @Test
    @DisplayName("buildRuleExtendedProperties()")
    void test03() {
        XmlRuleDefinitionBuilder builder = new XmlRuleDefinitionBuilder();
        Map<String, Object> map = new HashMap<>();
        {
            Assertions.assertEquals(0, builder.buildRuleExtendedProperties(map).size());
        }
        {
            Map<String, Object> extendedProperties = new HashMap<>();
            extendedProperties.put("key", "color");
            extendedProperties.put("value", "#000000");
            map.put("extended", extendedProperties);

            Map<String, Object> result = builder.buildRuleExtendedProperties(map);
            Assertions.assertEquals(1, result.size());
            Assertions.assertEquals("#000000", result.get("color"));
        }
        {
            List<Map<String, Object>> extended = new ArrayList<>();
            Map<String, Object> extended1 = new HashMap<>();
            extended1.put("key", "color");
            extended1.put("value", "#000000");
            Map<String, Object> extended2 = new HashMap<>();
            extended2.put("key", "action");
            extended2.put("value", "sms");
            extended.add(extended1);
            extended.add(extended2);
            map.put("extended", extended);

            Map<String, Object> result = builder.buildRuleExtendedProperties(map);
            Assertions.assertEquals(2, result.size());
            Assertions.assertEquals("#000000", result.get("color"));
            Assertions.assertEquals("sms", result.get("action"));
        }
    }

    @Test
    @DisplayName("doBuildRuleListDefinition()")
    void test06() throws NoSuchFieldException, IllegalAccessException {
        XmlRuleDefinitionBuilder builder = new XmlRuleDefinitionBuilder();
        ReflectionMemberAccessor reflectionMemberAccessor = new ReflectionMemberAccessor();
        reflectionMemberAccessor.set(FileRuleDefinitionBuilder.class.getDeclaredField("rootModel"), builder, Collections.EMPTY_MAP);

        Assertions.assertEquals(0, builder.doBuildRuleListDefinition(null).size());

        reflectionMemberAccessor.set(FileRuleDefinitionBuilder.class.getDeclaredField("rootModel"), builder, Collections.singletonMap("filterList", Collections.singletonMap("key", "value")));

        Assertions.assertEquals(1, builder.doBuildRuleListDefinition(null).size());
    }

    @Test
    @DisplayName("doBuildRuleDefinitions()")
    void test07() throws NoSuchFieldException, IllegalAccessException {
        XmlRuleDefinitionBuilder builder = new XmlRuleDefinitionBuilder();
        ReflectionMemberAccessor reflectionMemberAccessor = new ReflectionMemberAccessor();
        {
            reflectionMemberAccessor.set(FileRuleDefinitionBuilder.class.getDeclaredField("rootModel"), builder, Collections.EMPTY_MAP);
            Assertions.assertEquals(0, builder.doBuildRuleDefinitions(null).size());
        }
        {
            reflectionMemberAccessor.set(FileRuleDefinitionBuilder.class.getDeclaredField("rootModel"), builder, Collections.singletonMap("ruleSet", Collections.singletonMap("rule", new Object())));
            Assertions.assertEquals(1, builder.doBuildRuleDefinitions(null).size());
        }
        {
            List<Map<String, Object>> ruleSet = new ArrayList<>();
            Map<String, Object> rule1 = new HashMap<>();
            rule1.put("name", "风险账户10星");
            rule1.put("desc", "testing");
            Map<String, Object> rule2 = new HashMap<>();
            rule2.put("name", "风险账户9星");
            rule2.put("desc", "testing");
            ruleSet.add(rule1);
            ruleSet.add(rule2);
            reflectionMemberAccessor.set(FileRuleDefinitionBuilder.class.getDeclaredField("rootModel"), builder, Collections.singletonMap("ruleSet", Collections.singletonMap("rule", ruleSet)));
            List<Map<String, Object>> result = builder.doBuildRuleDefinitions(null);
            Assertions.assertEquals(2, result.size());

            Assertions.assertEquals("testing", result.get(0).get("desc"));
            Assertions.assertEquals("testing", result.get(1).get("desc"));
            Assertions.assertEquals("风险账户10星", result.get(0).get("name"));
            Assertions.assertEquals("风险账户9星", result.get(1).get("name"));
        }

    }

    @Test
    @DisplayName("doBuildRuleParamDefinitions()")
    void test08() throws NoSuchFieldException, IllegalAccessException {
        XmlRuleDefinitionBuilder builder = new XmlRuleDefinitionBuilder();
        ReflectionMemberAccessor reflectionMemberAccessor = new ReflectionMemberAccessor();
        {
            reflectionMemberAccessor.set(FileRuleDefinitionBuilder.class.getDeclaredField("rootModel"), builder, Collections.EMPTY_MAP);
            Assertions.assertEquals(0, builder.doBuildRuleParamDefinitions(null).size());
        }
        {
            reflectionMemberAccessor.set(FileRuleDefinitionBuilder.class.getDeclaredField("rootModel"), builder, Collections.singletonMap("params", Collections.singletonMap("param", new Object())));
            Assertions.assertEquals(1, builder.doBuildRuleParamDefinitions(null).size());
        }
        {
            List<Map<String, Object>> ruleSet = new ArrayList<>();
            Map<String, Object> rule1 = new HashMap<>();
            rule1.put("name", "手机号归属地");
            rule1.put("expression", "string.substring(v_phone, 7)");
            Map<String, Object> rule2 = new HashMap<>();
            rule2.put("name", "金额类型转换");
            rule2.put("expression", "decimal(v_amount)");
            ruleSet.add(rule1);
            ruleSet.add(rule2);
            reflectionMemberAccessor.set(FileRuleDefinitionBuilder.class.getDeclaredField("rootModel"), builder, Collections.singletonMap("params", Collections.singletonMap("param", ruleSet)));
            List<Map<String, Object>> result = builder.doBuildRuleParamDefinitions(null);
            Assertions.assertEquals(2, result.size());

            Assertions.assertEquals("string.substring(v_phone, 7)", result.get(0).get("expression"));
            Assertions.assertEquals("decimal(v_amount)", result.get(1).get("expression"));
            Assertions.assertEquals("手机号归属地", result.get(0).get("name"));
            Assertions.assertEquals("金额类型转换", result.get(1).get("name"));
        }

    }

    @Test
    @DisplayName("doBuildRuleDataSourceDefinitions()")
    void test09() throws NoSuchFieldException, IllegalAccessException {
        XmlRuleDefinitionBuilder builder = new XmlRuleDefinitionBuilder();
        ReflectionMemberAccessor reflectionMemberAccessor = new ReflectionMemberAccessor();

        reflectionMemberAccessor.set(FileRuleDefinitionBuilder.class.getDeclaredField("rootModel"), builder, Collections.EMPTY_MAP);
        Assertions.assertEquals(0, builder.doBuildRuleDataSourceDefinitions(null).size());

        Map<String, Object> database1 = new HashMap<>();
        database1.put("sql", "select * from dual");
        database1.put("resultClass", "com.test.Foo");
        database1.put("resultType", "single");
        Map<String, Object> database2 = new HashMap<>();
        database2.put("sql", "select * from dual");
        database2.put("resultClass", "com.test.Bar");
        database2.put("resultType", "list");
        List<Map<String, Object>> databases = new ArrayList<>();
        databases.add(database1);
        databases.add(database2);
        Map<String, Object> redis = new HashMap<>();
        redis.put("type", "hash");
        redis.put("key", "test:test");
        redis.put("hashKey", "test1");
        redis.put("setKey", "test2");
        Map<String, Object> dataSource = new HashMap<>();
        dataSource.put("database", databases);
        dataSource.put("redis", redis);

        Map<String, Object> rootModel = new HashMap<>();
        rootModel.put("dataSource", dataSource);
        reflectionMemberAccessor.set(FileRuleDefinitionBuilder.class.getDeclaredField("rootModel"), builder, rootModel);
        List<Map<String, Object>> result = builder.doBuildRuleDataSourceDefinitions(null);

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals("database", result.get(0).get("type"));
        Map<String, Object> config0 = (Map<String, Object>) result.get(0).get("config");
        Assertions.assertEquals("select * from dual", config0.get("sql"));
        Assertions.assertEquals("com.test.Foo", config0.get("resultClass"));
        Assertions.assertEquals("single", config0.get("resultType"));

        Assertions.assertEquals("database", result.get(1).get("type"));
        Map<String, Object> config1 = (Map<String, Object>) result.get(1).get("config");
        Assertions.assertEquals("select * from dual", config1.get("sql"));
        Assertions.assertEquals("com.test.Bar", config1.get("resultClass"));
        Assertions.assertEquals("list", config1.get("resultType"));

        Assertions.assertEquals("redis", result.get(2).get("type"));
        Map<String, Object> config2 = (Map<String, Object>) result.get(2).get("config");
        Assertions.assertEquals("hash", config2.get("type"));
        Assertions.assertEquals("test:test", config2.get("key"));
        Assertions.assertEquals("test2", config2.get("setKey"));
        Assertions.assertEquals("test1", config2.get("hashKey"));
    }

    @Test
    @DisplayName("构造函数")
    void test11() throws NoSuchFieldException, IllegalAccessException {
        String classpath = "risk";
        XmlRuleDefinitionBuilder builder = new XmlRuleDefinitionBuilder(classpath);
        ReflectionMemberAccessor reflectionMemberAccessor = new ReflectionMemberAccessor();
        Assertions.assertEquals(classpath, reflectionMemberAccessor.get(FileRuleDefinitionBuilder.class.getDeclaredField("classpath"), builder));
    }

    @Test
    @DisplayName("initRootModel()")
    void test12() {
        MockedStatic<XmlUtil> mockedStatic = Mockito.mockStatic(XmlUtil.class);
        Map<String, Object> rootModel = new HashMap<>();
        Map<String, Object> result = Collections.singletonMap("alatka-rule", rootModel);
        mockedStatic.when(() -> XmlUtil.getMap(Mockito.any(), Mockito.any())).thenReturn(result);

        FileWrapper path = Mockito.mock(FileWrapper.class);
//        Mockito.when(path.toFile()).thenReturn(Mockito.mock(File.class));
        XmlRuleDefinitionBuilder builder = new XmlRuleDefinitionBuilder();
        Assertions.assertSame(rootModel, builder.initRootModel(path));
        mockedStatic.close();
    }

    @Test
    @DisplayName("getValueWithMapOrThrow()")
    void test13() {
        XmlRuleDefinitionBuilder builder = new XmlRuleDefinitionBuilder();
        Map<String, Object> map = new HashMap<>();
        Assertions.assertThrows(IllegalArgumentException.class, () -> builder.getValueWithMapOrThrow(map, "none"), "No such key: none");

        map.put("key1", "value1");
        Assertions.assertEquals("value1", builder.getValueWithMapOrThrow(map, "key1"));
        map.put("key2", "true");
        Assertions.assertEquals(true, builder.getValueWithMapOrThrow(map, "key2"));
        map.put("key3", "false");
        Assertions.assertEquals(false, builder.getValueWithMapOrThrow(map, "key3"));
    }
}
