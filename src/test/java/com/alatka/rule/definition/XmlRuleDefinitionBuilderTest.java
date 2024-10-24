package com.alatka.rule.definition;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
    @Disabled
    void test03() {
        // TODO
    }
}
