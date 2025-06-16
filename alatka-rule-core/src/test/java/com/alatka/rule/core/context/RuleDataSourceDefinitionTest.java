package com.alatka.rule.core.context;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RuleDataSourceDefinitionTest {

    @Test
    @DisplayName("getter setter")
    void test01() {
        String id = "13";
        String name = "测试组";
        boolean enabled = false;
        RuleDataSourceDefinition.Type type = RuleDataSourceDefinition.Type.database;
        RuleDataSourceDefinition.Scope scope = RuleDataSourceDefinition.Scope.rule;
        Map<String, String> config = new HashMap<>();
        RuleDataSourceDefinition definition = new RuleDataSourceDefinition();
        definition.setId(id);
        definition.setName(name);
        definition.setEnabled(enabled);
        definition.setType(type);
        definition.setScope(scope);
        definition.setConfig(config);

        Assertions.assertEquals(id, definition.getId());
        Assertions.assertEquals(name, definition.getName());
        Assertions.assertEquals(enabled, definition.isEnabled());
        Assertions.assertSame(type, definition.getType());
        Assertions.assertSame(scope, definition.getScope());
        Assertions.assertSame(config, definition.getConfig());
    }

    @Test
    @DisplayName("字段默认值")
    void test02() {
        RuleDataSourceDefinition definition = new RuleDataSourceDefinition();

        Assertions.assertNull(definition.getId());
        Assertions.assertNull(definition.getName());
        Assertions.assertTrue(definition.isEnabled());
        Assertions.assertNull(definition.getType());
        Assertions.assertNull(definition.getScope());
        Assertions.assertNull(definition.getConfig());
    }

    @Test
    @DisplayName("默认常量")
    void test03() {
        RuleDataSourceDefinition instance = RuleDataSourceDefinition.DEFAULT_INSTANCE;

        Assertions.assertNull(instance.getId());
        Assertions.assertEquals("默认数据源", instance.getName());
        Assertions.assertTrue(instance.isEnabled());
        Assertions.assertSame(RuleDataSourceDefinition.Type.current, instance.getType());
        Assertions.assertNull(instance.getScope());
        Assertions.assertNull(instance.getConfig());
    }

    @Test
    @DisplayName("RuleDataSourceDefinition#Type")
    void test04() {
        RuleDataSourceDefinition.Type type1 = RuleDataSourceDefinition.Type.valueOf(RuleDataSourceDefinition.Type.current.name());
        RuleDataSourceDefinition.Type type2 = RuleDataSourceDefinition.Type.valueOf(RuleDataSourceDefinition.Type.database.name());
        RuleDataSourceDefinition.Type type3 = RuleDataSourceDefinition.Type.valueOf(RuleDataSourceDefinition.Type.redis.name());
        RuleDataSourceDefinition.Type type4 = RuleDataSourceDefinition.Type.valueOf(RuleDataSourceDefinition.Type.elasticsearch.name());

        Assertions.assertEquals(type1, RuleDataSourceDefinition.Type.values()[0]);
        Assertions.assertEquals(type2, RuleDataSourceDefinition.Type.values()[1]);
        Assertions.assertEquals(type3, RuleDataSourceDefinition.Type.values()[2]);
        Assertions.assertEquals(type4, RuleDataSourceDefinition.Type.values()[3]);
        Assertions.assertEquals(4, RuleDataSourceDefinition.Type.values().length);
    }

    @Test
    @DisplayName("RuleDataSourceDefinition#Scope")
    void test05() {
        RuleDataSourceDefinition.Scope scope1 = RuleDataSourceDefinition.Scope.valueOf(RuleDataSourceDefinition.Scope.global.name());
        RuleDataSourceDefinition.Scope scope2 = RuleDataSourceDefinition.Scope.valueOf(RuleDataSourceDefinition.Scope.request.name());
        RuleDataSourceDefinition.Scope scope3 = RuleDataSourceDefinition.Scope.valueOf(RuleDataSourceDefinition.Scope.rule.name());

        Assertions.assertEquals(scope1, RuleDataSourceDefinition.Scope.values()[0]);
        Assertions.assertEquals(scope2, RuleDataSourceDefinition.Scope.values()[1]);
        Assertions.assertEquals(scope3, RuleDataSourceDefinition.Scope.values()[2]);
        Assertions.assertEquals(3, RuleDataSourceDefinition.Scope.values().length);
    }

    @Test
    @DisplayName("equals()")
    void test06() {
        RuleDataSourceDefinition definition1 = new RuleDataSourceDefinition();
        definition1.setId("1");
        RuleDataSourceDefinition definition2 = new RuleDataSourceDefinition();
        definition2.setId("1");
        RuleDataSourceDefinition definition3 = new RuleDataSourceDefinition();
        definition3.setId("3");

        Assertions.assertEquals(definition1, definition1);
        Assertions.assertEquals(definition1, definition2);
        Assertions.assertNotEquals(definition1, definition3);
        Assertions.assertNotEquals(definition1, null);
        Assertions.assertNotEquals(definition1, new Object());
    }

    @Test
    @DisplayName("hashcode()")
    void test07() {
        RuleDataSourceDefinition definition1 = new RuleDataSourceDefinition();
        definition1.setId("1");
        Assertions.assertEquals(Objects.hashCode("1"), definition1.hashCode());
    }
}

