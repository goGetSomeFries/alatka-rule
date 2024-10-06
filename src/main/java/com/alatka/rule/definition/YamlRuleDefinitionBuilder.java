package com.alatka.rule.definition;

import com.alatka.rule.context.RuleGroupDefinition;

import java.nio.file.Path;

public class YamlRuleDefinitionBuilder extends FileRuleDefinitionBuilder {

    public YamlRuleDefinitionBuilder(String classpath) {
        super(classpath);
    }

    @Override
    protected RuleGroupDefinition buildRuleGroupDefinition(Path source) {
        return null;
    }

    @Override
    protected String[] suffix() {
        return new String[]{".yaml", "yml"};
    }
}
