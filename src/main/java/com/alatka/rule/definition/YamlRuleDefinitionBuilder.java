package com.alatka.rule.definition;

import com.alatka.messages.util.YamlUtil;
import com.alatka.rule.context.RuleGroupDefinition;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Yaml文件规则构建器
 *
 * @author whocares
 */
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
    protected List<Map<String, Object>> doBuildRuleDataSourceDefinitions(RuleGroupDefinition ruleGroupDefinition) {
        return this.getValueWithMap(this.rootModel, "dataSource");
    }

    @Override
    protected List<Map<String, Object>> doBuildRuleDefinitions(RuleGroupDefinition ruleGroupDefinition) {
        return this.getValueWithMap(this.rootModel, "ruleSet");
    }

    @Override
    protected List<Map<String, Object>> doBuildRuleUnitDefinitions(Map<String, Object> ruleDefinition) {
        return this.getValueWithMap(ruleDefinition, "units");
    }

    @Override
    protected String[] suffix() {
        return new String[]{".yaml", "yml"};
    }

}
