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
                    context.setRuleGroup(ruleGroupDefinition, ruleDefinitions);
                });
    }

    @Override
    public void refresh() {

    }

    protected <S> S getValueWithMap(Map<String, Object> map, String key) {
        return (S) map.get(key);
    }

    protected <S> S getValueWithMap(Map<String, Object> map, String key, S defaultValue) {
        return (S) map.getOrDefault(key, defaultValue);
    }

    protected abstract List<T> getSources();

    protected abstract void preProcess(T source);

    protected abstract RuleGroupDefinition buildRuleGroupDefinition(T source);

    protected List<RuleDefinition> buildRuleDefinitions(RuleGroupDefinition ruleGroupDefinition) {
        List<Map<String, Object>> rules = this.doBuildRuleDefinitions(ruleGroupDefinition);
        return rules.stream()
                .map(this::buildRuleDefinition)
                .filter(RuleDefinition::isEnabled)
                .collect(Collectors.toList());
    }

    protected abstract List<Map<String, Object>> doBuildRuleDefinitions(RuleGroupDefinition ruleGroupDefinition);

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
