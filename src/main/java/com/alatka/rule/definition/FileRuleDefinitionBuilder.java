package com.alatka.rule.definition;

import com.alatka.rule.context.RuleDefinition;
import com.alatka.rule.context.RuleGroupDefinition;
import com.alatka.rule.context.RuleUnitDefinition;
import com.alatka.rule.util.FileUtil;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
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
    protected RuleGroupDefinition buildRuleGroupDefinition(Path source) {
        String fileName = source.toFile().getName();
        String id = fileName.substring(0, fileName.lastIndexOf(SUFFIX));
        String desc = this.getValueWithMap(this.rootModel, "desc");
        boolean enabled = this.getValueWithMap(this.rootModel, "enabled", true);

        RuleGroupDefinition ruleGroupDefinition = new RuleGroupDefinition();
        ruleGroupDefinition.setId(id);
        ruleGroupDefinition.setDesc(desc);
        ruleGroupDefinition.setEnabled(enabled);
        return ruleGroupDefinition;
    }

    @Override
    protected List<RuleDefinition> buildRuleDefinitions(Path source) {
        List<Map<String, Object>> rules = this.getValueWithMap(this.rootModel, this.rulesKey());
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

    @Override
    protected void preProcess(Path source) {
        this.rootModel = this.initRootModel(source);
    }

    protected abstract Map<String, Object> initRootModel(Path source);

    protected abstract String[] suffix();

    protected abstract String rulesKey();

    protected abstract String ruleUnitsKey();

    private RuleDefinition buildRuleDefinition(Map<String, Object> map) {
        String id = this.getValueWithMap(map, "id");
        String desc = this.getValueWithMap(map, "desc");
        String remark = this.getValueWithMap(map, "remark");
        boolean enabled = this.getValueWithMap(map, "enabled", true);
        List<Map<String, Object>> units = this.getValueWithMap(map, this.ruleUnitsKey());
        RuleUnitDefinition ruleUnitDefinition = this.buildRuleUnitDefinitionChain(units);

        RuleDefinition ruleDefinition = new RuleDefinition();
        ruleDefinition.setId(id);
        ruleDefinition.setEnabled(enabled);
        ruleDefinition.setDesc(desc);
        ruleDefinition.setRemark(remark);
        ruleDefinition.setRuleUnitDefinition(ruleUnitDefinition);
        return ruleDefinition;
    }

    private RuleUnitDefinition buildRuleUnitDefinitionChain(List<Map<String, Object>> units) {
        AtomicReference<RuleUnitDefinition> reference = new AtomicReference<>();
        units.stream()
                .sorted(Collections.reverseOrder())
                .map(map -> {
                    boolean enabled = this.getValueWithMap(map, "enabled", true);
                    RuleUnitDefinition.Type type = this.getValueWithMap(map, "type", RuleUnitDefinition.Type.default_);
                    String path = this.getValueWithMap(map, "path");
                    String expression = path == null ? this.getValueWithMap(map, "expression") : FileUtil.getFileContent(path);

                    RuleUnitDefinition ruleUnitDefinition = new RuleUnitDefinition();
                    ruleUnitDefinition.setEnabled(enabled);
                    ruleUnitDefinition.setType(type);
                    ruleUnitDefinition.setExpression(expression);
                    return ruleUnitDefinition;
                })
                .filter(RuleUnitDefinition::isEnabled)
                .peek(ruleUnitDefinition -> ruleUnitDefinition.setNext(reference.get()))
                .forEach(reference::set);
        return reference.get();
    }
}
