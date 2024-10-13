package com.alatka.rule;

import com.alatka.rule.definition.XmlRuleDefinitionBuilder;
import com.alatka.rule.definition.YamlRuleDefinitionBuilder;
import org.junit.jupiter.api.Test;

public class Test1 {

    @Test
    public void test() {
//        new YamlRuleDefinitionBuilder().build();
        new XmlRuleDefinitionBuilder().build();
    }
}
