package com.alatka.rule;

import com.alatka.rule.context.RuleDefinition;
import com.alatka.rule.datasource.DataSourceBuilderFactory;
import com.alatka.rule.datasource.DatabaseDataSourceBuilder;
import com.alatka.rule.definition.RuleDefinitionBuilder;
import com.alatka.rule.definition.YamlRuleDefinitionBuilder;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test1 {

    @Test
    public void test() {
//        new YamlRuleDefinitionBuilder().build();
        RuleDefinitionBuilder ruleDefinitionBuilder = new YamlRuleDefinitionBuilder();
        ruleDefinitionBuilder.build();

/*
        DataSource dataSource = null;
        DataSourceBuilderFactory factory = DataSourceBuilderFactory.getInstance();
        factory.init(new DatabaseDataSourceBuilder(dataSource));
*/

        Map<String, Object> param = new HashMap<>();
        param.put("test", "1");
        RuleEngine ruleEngine = new RuleEngine("riskAnalysis");
        List<RuleDefinition> result = ruleEngine.execute(param);
        System.out.println(result);
    }

}
