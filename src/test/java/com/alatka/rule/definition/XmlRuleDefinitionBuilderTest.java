package com.alatka.rule.definition;

import com.alatka.rule.util.XmlUtil;
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
    @DisplayName("")
    void test03() {
        // TODO
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
    @DisplayName("构造函数")
    void test07() throws NoSuchFieldException, IllegalAccessException {
        String classpath = "risk";
        XmlRuleDefinitionBuilder builder = new XmlRuleDefinitionBuilder(classpath);
        ReflectionMemberAccessor reflectionMemberAccessor = new ReflectionMemberAccessor();
        Assertions.assertEquals(classpath, reflectionMemberAccessor.get(FileRuleDefinitionBuilder.class.getDeclaredField("classpath"), builder));
    }

    @Test
    @DisplayName("initRootModel()")
    void test08() {
        MockedStatic<XmlUtil> mockedStatic = Mockito.mockStatic(XmlUtil.class);
        Map<String, Object> rootModel = new HashMap<>();
        Map<String, Object> result = Collections.singletonMap("alatka-rule", rootModel);
        mockedStatic.when(() -> XmlUtil.getMap(Mockito.any(), Mockito.any())).thenReturn(result);

        Path path = Mockito.mock(Path.class);
        Mockito.when(path.toFile()).thenReturn(Mockito.mock(File.class));
        XmlRuleDefinitionBuilder builder = new XmlRuleDefinitionBuilder();
        Assertions.assertSame(rootModel, builder.initRootModel(path));
        mockedStatic.close();
    }
}
