package com.alatka.rule.definition;

import com.alatka.messages.util.YamlUtil;

import java.nio.file.Path;
import java.util.Map;

public class YamlRuleDefinitionBuilder extends FileRuleDefinitionBuilder {

    private static final String YAML_ROOT_NAME = "alatka.rule";

    public YamlRuleDefinitionBuilder() {
    }

    public YamlRuleDefinitionBuilder(String classpath) {
        super(classpath);
    }

    @Override
    protected Map<String, Object> initRootModel(Path source) {
        return YamlUtil.getMap(source.toFile(), YAML_ROOT_NAME, Object.class);
    }

    @Override
    protected String rulesKey() {
        return "ruleSet";
    }

    @Override
    protected String ruleUnitsKey() {
        return "units";
    }

    @Override
    protected String[] suffix() {
        return new String[]{".yaml", "yml"};
    }

}
