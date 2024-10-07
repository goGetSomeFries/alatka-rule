package com.alatka.rule.definition;

import com.alatka.messages.util.YamlUtil;
import com.alatka.rule.context.RuleDefinition;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class YamlRuleDefinitionBuilder extends FileRuleDefinitionBuilder {

    private static final String YAML_ROOT_NAME = "alatka.rule";

    public YamlRuleDefinitionBuilder(String classpath) {
        super(classpath);
    }

    @Override
    protected Map<String, Object> doBuildRuleGroupDefinition(Path source) {
        return YamlUtil.getMap(source.toFile(), YAML_ROOT_NAME, Object.class);
    }

    @Override
    protected RuleDefinition buildRuleDefinition(Map<String, Object> map) {
        String desc = this.getValueWithMap(map, "desc");
        boolean enabled = this.getValueWithMap(map, "enabled", true);
        List<Map<String, Object>> elements = this.getValueWithMap(map, "elements");
        return null;
    }

    @Override
    protected String[] suffix() {
        return new String[]{".yaml", "yml"};
    }
}
