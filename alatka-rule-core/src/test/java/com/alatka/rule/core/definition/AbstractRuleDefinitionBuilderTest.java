package com.alatka.rule.core.definition;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class AbstractRuleDefinitionBuilderTest {

    @Test
    @DisplayName("getValueWithMapOrThrow()")
    void test01() {
        YamlRuleDefinitionBuilder builder = new YamlRuleDefinitionBuilder();
        Map<String, Object> map = new HashMap<>();
        Assertions.assertThrows(IllegalArgumentException.class, () -> builder.getValueWithMapOrThrow(map, "none"), "No such key: none");

        map.put("key1", "value1");
        Assertions.assertEquals("value1", builder.getValueWithMapOrThrow(map, "key1"));
    }
}
