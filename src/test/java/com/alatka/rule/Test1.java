package com.alatka.rule;

import com.alatka.messages.util.YamlUtil;
import com.alatka.rule.definition.RuleDefinitionBuilder;
import com.alatka.rule.definition.YamlRuleDefinitionBuilder;
import com.alatka.rule.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test1 {

    @Test
    public void test() {
//        new YamlRuleDefinitionBuilder().build();
        RuleDefinitionBuilder ruleDefinitionBuilder = new YamlRuleDefinitionBuilder();
        ruleDefinitionBuilder.build();
        RulesEngine rulesEngine = new RulesEngine("riskAnalysis");

        Map<String, Object> param = new HashMap<>();
        param.put("test", "1");
        List<String> result = rulesEngine.execute(param);
        System.out.println(result);
    }

    @Test
    public void test1() {
        List<Path> list = FileUtil.getClasspathFiles("", "riskAnalysis.rule.yaml");
        Map<String, Object> map = YamlUtil.getMap(list.get(0).toFile(), "alatka.rule.definition", Object.class);
        System.out.println(map);
    }
}
