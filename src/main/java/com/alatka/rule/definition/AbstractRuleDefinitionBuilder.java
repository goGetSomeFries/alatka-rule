package com.alatka.rule.definition;

import com.alatka.rule.context.*;
import com.alatka.rule.util.FileUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractRuleDefinitionBuilder<T> implements RuleDefinitionBuilder {

    private Map<String, RuleDataSourceDefinition> mapping;

    @Override
    public void build() {
        RuleGroupDefinitionContext context = RuleGroupDefinitionContext.getInstance();
        this.getSources().stream()
                .peek(this::preProcess)
                .map(this::buildRuleGroupDefinition)
                .filter(RuleGroupDefinition::isEnabled)
                .peek(ruleGroupDefinition -> this.mapping = this.buildRuleDataSourceDefinitionMap(ruleGroupDefinition))
                .forEach(ruleGroupDefinition -> {
                    List<RuleDefinition> ruleDefinitions = this.buildRuleDefinitions(ruleGroupDefinition);
                    context.initRuleDefinitions(ruleGroupDefinition, ruleDefinitions);
                });
        this.postProcess();
        this.mapping = null;
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

    private RuleDataSourceDefinition buildRuleDataSourceDefinition(Map<String, Object> map) {
        String id = this.getValueWithMapOrThrow(map, "id");
        String desc = this.getValueWithMapOrThrow(map, "desc");
        boolean enabled = this.getValueWithMap(map, "enabled", true);
        String type = this.getValueWithMapOrThrow(map, "type");
        String resultType = this.getValueWithMapOrThrow(map, "resultType");
        Map<String, Object> config = this.getValueWithMapOrThrow(map, "config");
        String scope = this.getValueWithMap(map, "scope", RuleDataSourceDefinition.Scope.data.name());

        RuleDataSourceDefinition definition = new RuleDataSourceDefinition();
        definition.setId(id);
        definition.setType(RuleDataSourceDefinition.Type.valueOf(type));
        definition.setScope(RuleDataSourceDefinition.Scope.valueOf(scope));
        definition.setResultType(RuleDataSourceDefinition.ResultType.valueOf(resultType));
        definition.setEnabled(enabled);
        definition.setDesc(desc);
        definition.setConfig(config);
        return definition;
    }

    private List<RuleDefinition> buildRuleDefinitions(RuleGroupDefinition ruleGroupDefinition) {
        List<Map<String, Object>> rules = this.doBuildRuleDefinitions(ruleGroupDefinition);
        return rules.stream()
                .map(map -> this.buildRuleDefinition(map))
                .filter(RuleDefinition::isEnabled)
                .collect(Collectors.toList());
    }

    private RuleDefinition buildRuleDefinition(Map<String, Object> map) {
        String id = this.getValueWithMapOrThrow(map, "id");
        String desc = this.getValueWithMapOrThrow(map, "desc");
        String remark = this.getValueWithMapOrThrow(map, "remark");
        int priority = this.getValueWithMap(map, "priority", 1);
        boolean enabled = this.getValueWithMap(map, "enabled", true);

        List<Map<String, Object>> units = this.doBuildRuleUnitDefinitions(map);
        RuleUnitDefinition ruleUnitDefinition = this.buildRuleUnitDefinition(units);

        RuleDefinition ruleDefinition = new RuleDefinition();
        ruleDefinition.setId(id);
        ruleDefinition.setEnabled(enabled);
        ruleDefinition.setDesc(desc);
        ruleDefinition.setRemark(remark);
        ruleDefinition.setPriority(priority);
        ruleDefinition.setRuleUnitDefinition(ruleUnitDefinition);
        return ruleDefinition;
    }

    private RuleUnitDefinition buildRuleUnitDefinition(List<Map<String, Object>> units
    ) {
        List<Map<String, Object>> list = new ArrayList<>(units);
        Collections.reverse(list);

        AtomicReference<RuleUnitDefinition> reference = new AtomicReference<>();
        list.stream()
                .map(this::doBuildRuleUnitDefinition)
                .filter(RuleUnitDefinition::isEnabled)
                .peek(ruleUnitDefinition -> ruleUnitDefinition.setNext(reference.get()))
                .forEach(reference::set);
        return reference.get();
    }

    private RuleUnitDefinition doBuildRuleUnitDefinition(Map<String, Object> map) {
        boolean enabled = this.getValueWithMap(map, "enabled", true);
        String path = this.getValueWithMap(map, "path");
        String expression = path == null ? this.getValueWithMapOrThrow(map, "expression") : FileUtil.getFileContent(path);
        String dataSource = this.getValueWithMap(map, "dataSource");
        RuleDataSourceDefinition dataSrouceRef = null;
        if (dataSource != null) {
            dataSrouceRef = this.mapping.get(dataSource);
            if (dataSrouceRef == null) {
                throw new IllegalArgumentException("DataSource '" + dataSource + "' not found");
            }
        }

        RuleUnitDefinition ruleUnitDefinition = new RuleUnitDefinition();
        ruleUnitDefinition.setEnabled(enabled);
        ruleUnitDefinition.setDataSourceRef(dataSrouceRef);
        ruleUnitDefinition.setExpression(expression);
        return ruleUnitDefinition;
    }

    private Map<String, RuleDataSourceDefinition> buildRuleDataSourceDefinitionMap(RuleGroupDefinition ruleGroupDefinition) {
        List<Map<String, Object>> ruleDataSources = this.doBuildRuleDataSourceDefinitions(ruleGroupDefinition);
        return ruleDataSources.stream()
                .map(this::buildRuleDataSourceDefinition)
                .filter(RuleDataSourceDefinition::isEnabled)
                .collect(Collectors.toMap(RuleDataSourceDefinition::getId, Function.identity()));
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

    protected abstract List<Map<String, Object>> doBuildRuleDataSourceDefinitions(RuleGroupDefinition ruleGroupDefinition);

    protected abstract List<Map<String, Object>> doBuildRuleDefinitions(RuleGroupDefinition ruleGroupDefinition);

    protected abstract List<Map<String, Object>> doBuildRuleUnitDefinitions(Map<String, Object> ruleDefinition);

    protected abstract void postProcess();

}
