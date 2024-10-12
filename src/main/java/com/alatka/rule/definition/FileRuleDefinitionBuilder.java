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

    private Map<String, Object> rootModel;

    public FileRuleDefinitionBuilder() {
        this("");
    }

    public FileRuleDefinitionBuilder(String classpath) {
        this.classpath = classpath;
    }

    @Override
    protected Map<String, Object> doBuildRuleGroupDefinition(Path source) {
        String fileName = source.toFile().getName();

        String id = fileName.substring(0, fileName.lastIndexOf(SUFFIX));
        String desc = this.getValueWithMap(this.rootModel, "desc");
        String type = this.getValueWithMap(this.rootModel, "type");
        boolean enabled = this.getValueWithMap(this.rootModel, "enabled");

        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("desc", desc);
        result.put("enabled", enabled);
        result.put("type", type);
        return result;
    }

    @Override
    protected List<Map<String, Object>> doBuildRuleDefinitions(RuleGroupDefinition ruleGroupDefinition) {
        return this.getValueWithMap(this.rootModel, this.rulesKey());
    }

    @Override
    protected List<Map<String, Object>> doBuildRuleUnitDefinitions(RuleDefinition ruleDefinition) {
        return this.getValueWithMap(this.rootModel, this.ruleUnitsKey());
    }

    @Override
    protected List<Path> getSources() {
        return Arrays.stream(suffix())
                .flatMap(suffix -> FileUtil.getClasspathFiles(this.classpath, SUFFIX + suffix).stream())
                .collect(Collectors.toList());
    }

    @Override
    protected void preProcess(Path source) {
        this.rootModel = this.initRootModel(source);
    }

    @Override
    protected void postProcess() {
        this.rootModel = null;
    }

    protected abstract Map<String, Object> initRootModel(Path source);

    protected abstract String[] suffix();

    protected abstract String rulesKey();

    protected abstract String ruleUnitsKey();

}
