package com.alatka.rule.definition;

import com.alatka.rule.datasource.ExternalDataSourceFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.reflection.ReflectionMemberAccessor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class YamlRuleDefinitionBuilderTest {

    @Test
    @DisplayName("suffix()")
    void test01() {
        YamlRuleDefinitionBuilder builder = new YamlRuleDefinitionBuilder();

        Assertions.assertEquals(".yaml", builder.suffix()[0]);
        Assertions.assertEquals(".yml", builder.suffix()[1]);
    }

    @Test
    @DisplayName("doBuildRuleUnitDefinitions()")
    void test02() {
        YamlRuleDefinitionBuilder builder = new YamlRuleDefinitionBuilder();
        Map<String, Object> map = new HashMap<>();
        Assertions.assertEquals(0, builder.doBuildRuleUnitDefinitions(map).size());

        map.put("units", Collections.singletonList(Collections.singletonMap("key", "value")));
        Assertions.assertEquals(1, builder.doBuildRuleUnitDefinitions(map).size());
    }

    @Test
    @DisplayName("doBuildRuleDefinitions()")
    void test03() throws NoSuchFieldException, IllegalAccessException {
        YamlRuleDefinitionBuilder builder = new YamlRuleDefinitionBuilder();
        ReflectionMemberAccessor reflectionMemberAccessor = new ReflectionMemberAccessor();
        reflectionMemberAccessor.set(FileRuleDefinitionBuilder.class.getDeclaredField("rootModel"), builder, Collections.EMPTY_MAP);

        Assertions.assertEquals(0, builder.doBuildRuleDefinitions(null).size());

        reflectionMemberAccessor.set(FileRuleDefinitionBuilder.class.getDeclaredField("rootModel"), builder, Collections.singletonMap("ruleSet", Collections.singletonList(Collections.singletonMap("key", "value"))));

        Assertions.assertEquals(1, builder.doBuildRuleDefinitions(null).size());
    }

    @Test
    @DisplayName("doBuildRuleDataSourceDefinitions()")
    void test04() throws NoSuchFieldException, IllegalAccessException {
        YamlRuleDefinitionBuilder builder = new YamlRuleDefinitionBuilder();
        ReflectionMemberAccessor reflectionMemberAccessor = new ReflectionMemberAccessor();
        reflectionMemberAccessor.set(FileRuleDefinitionBuilder.class.getDeclaredField("rootModel"), builder, Collections.EMPTY_MAP);

        Assertions.assertEquals(0, builder.doBuildRuleDataSourceDefinitions(null).size());

        reflectionMemberAccessor.set(FileRuleDefinitionBuilder.class.getDeclaredField("rootModel"), builder, Collections.singletonMap("dataSource", Collections.singletonList(Collections.singletonMap("key", "value"))));

        Assertions.assertEquals(1, builder.doBuildRuleDataSourceDefinitions(null).size());
    }
}
