package com.alatka.rule.definition;

import com.alatka.rule.context.RuleGroupDefinition;
import com.alatka.rule.util.XmlUtil;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class XmlRuleDefinitionBuilder extends FileRuleDefinitionBuilder {

    public XmlRuleDefinitionBuilder() {
    }

    public XmlRuleDefinitionBuilder(String classpath) {
        super(classpath);
    }

    @Override
    protected Map<String, Object> initRootModel(Path source) {
        Map<String, Object> map = XmlUtil.getMap(source.toFile(), Object.class);
        Map<String, Object> rootModel = this.getValueWithMap(map, "ruleSet");
        return rootModel;
    }

    @Override
    protected List<Map<String, Object>> doBuildRuleDataSourceDefinitions(RuleGroupDefinition ruleGroupDefinition) {
        return this.getValueWithMap(this.rootModel, "dataSource");
    }

    @Override
    protected List<Map<String, Object>> doBuildRuleDefinitions(RuleGroupDefinition ruleGroupDefinition) {
        Object object = this.getValueWithMap(this.rootModel, "rule");
        return (List<Map<String, Object>>) (object instanceof List ? object : Collections.singletonList(object));
    }

    @Override
    protected List<Map<String, Object>> doBuildRuleUnitDefinitions(Map<String, Object> ruleDefinition) {
        Object object = this.getValueWithMap(ruleDefinition, "unit");
        return (List<Map<String, Object>>) (object instanceof List ? object : Collections.singletonList(object));
    }

    @Override
    protected String[] suffix() {
        return new String[]{".xml"};
    }

    @Override
    protected <S> S getValueWithMap(Map<String, Object> map, String key) {
        Object object = map.get(key);
        return (S) convert(object);
    }

    @Override
    protected <S> S getValueWithMap(Map<String, Object> map, String key, S defaultValue) {
        Object object = map.getOrDefault(key, defaultValue);
        return (S) convert(object);
    }

    @Override
    protected <S> S getValueWithMapOrThrow(Map<String, Object> map, String key) {
        if (!map.containsKey(key)) {
            throw new IllegalArgumentException("No such key: " + key);
        }
        Object object = map.get(key);
        return (S) convert(object);
    }

    private Object convert(Object object) {
        return object instanceof String && object.toString().matches("true|false") ?
                Boolean.valueOf(object.toString()) : object;
    }
}
