package com.alatka.rule;

import com.alatka.messages.util.YamlUtil;
import com.alatka.rule.definition.XmlRuleDefinitionBuilder;
import com.alatka.rule.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Test1 {

    @Test
    public void test() {
//        new YamlRuleDefinitionBuilder().build();
        new XmlRuleDefinitionBuilder().build();
    }

    @Test
    public void test1() {
        List<Path> list = FileUtil.getClasspathFiles("", "riskAnalysis.rule.yaml");
        Map<String, Object> map = YamlUtil.getMap(list.get(0).toFile(), "alatka.rule.definition", Object.class);
        System.out.println(map);
    }
}
