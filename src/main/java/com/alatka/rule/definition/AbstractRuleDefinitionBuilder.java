package com.alatka.rule.definition;

import com.alatka.rule.context.RuleDefinition;
import com.alatka.rule.context.RuleDefinitionContext;
import com.alatka.rule.context.RuleGroupDefinition;
import com.alatka.rule.context.RuleUnitDefinition;
import com.alatka.rule.util.FileUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public abstract class AbstractRuleDefinitionBuilder<T> implements RuleDefinitionBuilder {

    @Override
    public void build() {
        RuleDefinitionContext context = RuleDefinitionContext.getInstance();
        this.getSources().stream()
                .peek(this::preProcess)
                .map(this::buildRuleGroupDefinition)
                .filter(RuleGroupDefinition::isEnabled)
                .forEach(ruleGroupDefinition -> {
                    List<RuleDefinition> ruleDefinitions = this.buildRuleDefinitions(ruleGroupDefinition);
                    context.init(ruleGroupDefinition, ruleDefinitions);
                });
        this.postProcess();
    }

    @Override
    public void refresh() {

    }

    private RuleGroupDefinition buildRuleGroupDefinition(T source) {
        Map<String, Object> map = this.doBuildRuleGroupDefinition(source);
        String id = this.getValueWithMapOrThrow(map, "id");
        String desc = this.getValueWithMapOrThrow(map, "desc");
        String type = this.getValueWithMap(map, "type", RuleGroupDefinition.Type.greedy.name());
        boolean enabled = this.getValueWithMap(map, "enabled", true);

        RuleGroupDefinition ruleGroupDefinition = new RuleGroupDefinition();
        ruleGroupDefinition.setId(id);
        ruleGroupDefinition.setDesc(desc);
        ruleGroupDefinition.setType(RuleGroupDefinition.Type.valueOf(type));
        ruleGroupDefinition.setEnabled(enabled);
        return ruleGroupDefinition;
    }

    protected List<RuleDefinition> buildRuleDefinitions(RuleGroupDefinition ruleGroupDefinition) {
        List<Map<String, Object>> rules = this.doBuildRuleDefinitions(ruleGroupDefinition);
        return rules.stream()
                .map(this::buildRuleDefinition)
                .filter(RuleDefinition::isEnabled)
                .collect(Collectors.toList());
    }

    private RuleDefinition buildRuleDefinition(Map<String, Object> map) {
        String id = this.getValueWithMapOrThrow(map, "id");
        String desc = this.getValueWithMapOrThrow(map, "desc");
        String remark = this.getValueWithMapOrThrow(map, "remark");
        int priority = this.getValueWithMap(map, "priority", 1);
        boolean enabled = this.getValueWithMap(map, "enabled", true);

        RuleDefinition ruleDefinition = new RuleDefinition();
        ruleDefinition.setId(id);
        ruleDefinition.setEnabled(enabled);
        ruleDefinition.setDesc(desc);
        ruleDefinition.setRemark(remark);
        ruleDefinition.setPriority(priority);

        List<Map<String, Object>> units = this.doBuildRuleUnitDefinitions(ruleDefinition);
        RuleUnitDefinition ruleUnitDefinition = this.buildRuleUnitDefinition(units);

        ruleDefinition.setRuleUnitDefinition(ruleUnitDefinition);
        return ruleDefinition;
    }

    private RuleUnitDefinition buildRuleUnitDefinition(List<Map<String, Object>> units) {
        AtomicReference<RuleUnitDefinition> reference = new AtomicReference<>();
        units.stream()
                .sorted(Collections.reverseOrder())
                .map(map -> {
                    boolean enabled = this.getValueWithMap(map, "enabled", true);
                    RuleUnitDefinition.Type type = this.getValueWithMap(map, "type", RuleUnitDefinition.Type.default_);
                    String path = this.getValueWithMap(map, "path");
                    String expression = path == null ? this.getValueWithMapOrThrow(map, "expression") : FileUtil.getFileContent(path);

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

    protected <S> S getValueWithMap(Map<String, Object> map, String key) {
        return (S) map.get(key);
    }

    protected <S> S getValueWithMap(Map<String, Object> map, String key, S defaultValue) {
        return (S) map.getOrDefault(key, defaultValue);
    }

    protected <S> S getValueWithMapOrThrow(Map<String, Object> map, String key) {
        if (!map.containsKey(key)) {
            throw new IllegalArgumentException("No such key: " + key);
        }
        return (S) map.get(key);
    }

    protected abstract List<T> getSources();

    protected abstract void preProcess(T source);

    protected abstract Map<String, Object> doBuildRuleGroupDefinition(T source);

    protected abstract List<Map<String, Object>> doBuildRuleDefinitions(RuleGroupDefinition ruleGroupDefinition);

    protected abstract List<Map<String, Object>> doBuildRuleUnitDefinitions(RuleDefinition ruleDefinition);

    protected abstract void postProcess();

}
