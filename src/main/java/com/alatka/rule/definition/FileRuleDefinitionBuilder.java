package com.alatka.rule.definition;

import com.alatka.rule.context.RuleDefinition;
import com.alatka.rule.context.RuleGroupDefinition;
import com.alatka.rule.util.FileUtil;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class FileRuleDefinitionBuilder extends AbstractRuleDefinitionBuilder<Path> {

    private static final String SUFFIX = ".rule";

    private String classpath;

    public FileRuleDefinitionBuilder() {
        this.classpath = "";
    }

    public FileRuleDefinitionBuilder(String classpath) {
        this.classpath = classpath;
    }

    @Override
    protected RuleGroupDefinition buildRuleGroupDefinition(Path source) {
        Map<String, Object> map = this.doBuildRuleGroupDefinition(source);
        RuleGroupDefinition ruleGroupDefinition = new RuleGroupDefinition();

        String fileName = source.toFile().getName();
        String id = fileName.substring(0, fileName.lastIndexOf(SUFFIX));
        ruleGroupDefinition.setId(id);
        ruleGroupDefinition.setDesc((String) map.getOrDefault("desc", ""));
        ruleGroupDefinition.setEnabled((boolean) map.getOrDefault("enabled", true));
        return ruleGroupDefinition;
    }

    @Override
    protected List<RuleDefinition> buildRuleDefinitions(Path source) {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> rules = this.getValueWithMap(map, "ruleSet");
        return rules.stream()
                .map(this::buildRuleDefinition)
                .filter(RuleDefinition::isEnabled)
                .collect(Collectors.toList());
    }

    @Override
    protected List<Path> getSources() {
        return Arrays.stream(suffix())
                .flatMap(suffix -> FileUtil.getClasspathFiles(this.classpath, SUFFIX + suffix).stream())
                .collect(Collectors.toList());
    }

    protected <T> T getValueWithMap(Map<String, Object> map, String key) {
        return (T) map.get(key);
    }

    protected <T> T getValueWithMap(Map<String, Object> map, String key, T defaultValue) {
        return (T) map.getOrDefault(key, defaultValue);
    }

    protected abstract Map<String, Object> doBuildRuleGroupDefinition(Path source);

    protected abstract RuleDefinition buildRuleDefinition(Map<String, Object> map);

    protected abstract String[] suffix();

}
