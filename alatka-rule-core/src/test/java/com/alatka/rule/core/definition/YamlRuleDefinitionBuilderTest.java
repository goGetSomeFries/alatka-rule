package com.alatka.rule.core.definition;

import com.alatka.rule.core.util.YamlUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.ReflectionMemberAccessor;

import java.io.File;
import java.nio.file.Path;
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

    @Test
    @DisplayName("doBuildRuleParamDefinitions()")
    void test05() throws NoSuchFieldException, IllegalAccessException {
        YamlRuleDefinitionBuilder builder = new YamlRuleDefinitionBuilder();
        ReflectionMemberAccessor reflectionMemberAccessor = new ReflectionMemberAccessor();
        reflectionMemberAccessor.set(FileRuleDefinitionBuilder.class.getDeclaredField("rootModel"), builder, Collections.EMPTY_MAP);

        Assertions.assertEquals(0, builder.doBuildRuleParamDefinitions(null).size());

        reflectionMemberAccessor.set(FileRuleDefinitionBuilder.class.getDeclaredField("rootModel"), builder, Collections.singletonMap("params", Collections.singletonList(Collections.singletonMap("key", "value"))));

        Assertions.assertEquals(1, builder.doBuildRuleParamDefinitions(null).size());
    }

    @Test
    @DisplayName("doBuildRuleListDefinition()")
    void test06() throws NoSuchFieldException, IllegalAccessException {
        YamlRuleDefinitionBuilder builder = new YamlRuleDefinitionBuilder();
        ReflectionMemberAccessor reflectionMemberAccessor = new ReflectionMemberAccessor();
        reflectionMemberAccessor.set(FileRuleDefinitionBuilder.class.getDeclaredField("rootModel"), builder, Collections.EMPTY_MAP);

        Assertions.assertEquals(0, builder.doBuildRuleListDefinition(null).size());

        reflectionMemberAccessor.set(FileRuleDefinitionBuilder.class.getDeclaredField("rootModel"), builder, Collections.singletonMap("filterList", Collections.singletonMap("key", "value")));

        Assertions.assertEquals(1, builder.doBuildRuleListDefinition(null).size());
    }

    @Test
    @DisplayName("构造函数")
    void test07() throws NoSuchFieldException, IllegalAccessException {
        String classpath = "risk";
        YamlRuleDefinitionBuilder builder = new YamlRuleDefinitionBuilder(classpath);
        ReflectionMemberAccessor reflectionMemberAccessor = new ReflectionMemberAccessor();
        Assertions.assertEquals(classpath, reflectionMemberAccessor.get(FileRuleDefinitionBuilder.class.getDeclaredField("classpath"), builder));
    }

    @Test
    @DisplayName("initRootModel()")
    void test08() {
        MockedStatic<YamlUtil> mockedStatic = Mockito.mockStatic(YamlUtil.class);
        HashMap<String, Object> result = new HashMap<>(0);
        mockedStatic.when(() -> YamlUtil.getMap(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(result);

        Path path = Mockito.mock(Path.class);
        Mockito.when(path.toFile()).thenReturn(Mockito.mock(File.class));
        YamlRuleDefinitionBuilder builder = new YamlRuleDefinitionBuilder();
        Assertions.assertSame(result, builder.initRootModel(path));
        mockedStatic.close();
    }
}
