package com.alatka.rule.core;

import com.alatka.rule.core.context.RuleDefinition;
import com.alatka.rule.core.definition.RuleDefinitionBuilder;
import com.alatka.rule.core.definition.XmlRuleDefinitionBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test1 {

    @Test
    @Disabled
    public void test() {
//        new YamlRuleDefinitionBuilder().build();
        RuleDefinitionBuilder ruleDefinitionBuilder = new XmlRuleDefinitionBuilder();
        ruleDefinitionBuilder.build();

/*
        DataSource dataSource = null;
        DataSourceBuilderFactory factory = DataSourceBuilderFactory.getInstance();
        factory.init(new DatabaseDataSourceBuilder(dataSource));
*/

        Map<String, Object> param = new HashMap<>();
        param.put("test", "1");
        param.put("v_amount", 200);
        param.put("v_card", "18911119834");
        RuleEngine ruleEngine = new RuleEngine();
        List<RuleDefinition> result = ruleEngine.execute("riskAnalysis", param);
        System.out.println(result);
    }

}
