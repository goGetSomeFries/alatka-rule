package com.alatka.rule.core.definition;

import com.alatka.rule.core.support.FileWrapper;
import com.alatka.rule.core.util.FileUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.ReflectionMemberAccessor;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileRuleDefinitionBuilderTest {

    @Test
    @DisplayName("postProcess()")
    void test01() throws NoSuchFieldException, IllegalAccessException {
        FileRuleDefinitionBuilder builder = new YamlRuleDefinitionBuilder();
        ReflectionMemberAccessor reflectionMemberAccessor = new ReflectionMemberAccessor();
        Field field = FileRuleDefinitionBuilder.class.getDeclaredField("rootModel");
        reflectionMemberAccessor.set(field, builder, Collections.EMPTY_MAP);

        Assertions.assertNotNull(reflectionMemberAccessor.get(field, builder));

        builder.postProcess();
        Assertions.assertNull(reflectionMemberAccessor.get(field, builder));
    }

    @Test
    @DisplayName("preProcess()")
    void test02() throws NoSuchFieldException, IllegalAccessException {
        FileRuleDefinitionBuilder builder = Mockito.spy(FileRuleDefinitionBuilder.class);
        HashMap<String, Object> map = new HashMap<>(0);
        Mockito.when(builder.initRootModel(Mockito.any())).thenReturn(map);

        builder.doPreProcess(null);
        ReflectionMemberAccessor reflectionMemberAccessor = new ReflectionMemberAccessor();
        Assertions.assertSame(map, reflectionMemberAccessor.get(FileRuleDefinitionBuilder.class.getDeclaredField("rootModel"), builder));
    }

    @Test
    @DisplayName("doBuildRuleGroupDefinition()")
    void test03() throws NoSuchFieldException, IllegalAccessException {
        FileWrapper path = Mockito.mock(FileWrapper.class);
        Mockito.when(path.getName()).thenReturn("test.rule.yml");

        FileRuleDefinitionBuilder builder = Mockito.spy(FileRuleDefinitionBuilder.class);
        Map<String, Object> map = Mockito.mock(Map.class);
        Mockito.when(map.get("name")).thenReturn("testing");
        Mockito.when(map.getOrDefault("type", "all")).thenReturn("all");
        Mockito.when(map.getOrDefault("enabled", true)).thenReturn(false);


        ReflectionMemberAccessor reflectionMemberAccessor = new ReflectionMemberAccessor();
        Field field = FileRuleDefinitionBuilder.class.getDeclaredField("rootModel");
        reflectionMemberAccessor.set(field, builder, map);

        Map<String, Object> actual = builder.doBuildRuleGroupDefinition(path);
        Assertions.assertEquals("test", actual.get("id"));
        Assertions.assertEquals("testing", actual.get("name"));
        Assertions.assertEquals("all", actual.get("type"));
        Assertions.assertEquals(false, actual.get("enabled"));
    }

    @Test
    @DisplayName("getSources()")
    void test04() {
        FileRuleDefinitionBuilder builder = Mockito.spy(FileRuleDefinitionBuilder.class);
        Mockito.when(builder.suffix()).thenReturn(new String[]{"yaml", "yml"});

        FileWrapper path1 = Mockito.mock(FileWrapper.class);
        FileWrapper path2 = Mockito.mock(FileWrapper.class);
        List<FileWrapper> list = Stream.of(path1, path2).collect(Collectors.toList());

        MockedStatic<FileUtil> mockedStatic = Mockito.mockStatic(FileUtil.class);
        mockedStatic.when(() -> FileUtil.getFilesContent(Mockito.anyString(), Mockito.anyString())).thenReturn(list);

        Assertions.assertEquals(4, builder.getSources().size());
        mockedStatic.close();
    }
}
