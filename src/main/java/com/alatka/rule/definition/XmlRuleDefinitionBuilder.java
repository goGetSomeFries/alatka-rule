package com.alatka.rule.definition;

import com.alatka.rule.context.RuleDataSourceDefinition;
import com.alatka.rule.context.RuleGroupDefinition;
import com.alatka.rule.util.XmlUtil;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Xml文件规则构建器
 *
 * @author whocares
 */
public class XmlRuleDefinitionBuilder extends FileRuleDefinitionBuilder {

    public XmlRuleDefinitionBuilder() {
    }

    public XmlRuleDefinitionBuilder(String classpath) {
        super(classpath);
    }

    @Override
    protected Map<String, Object> initRootModel(Path source) {
        Map<String, Object> map = XmlUtil.getMap(source.toFile(), Object.class);
        Map<String, Object> rootModel = this.getValueWithMap(map, "alatka-rule");
        return rootModel;
    }

    @Override
    protected List<Map<String, Object>> doBuildRuleDataSourceDefinitions(RuleGroupDefinition ruleGroupDefinition) {
        Map<String, Object> dataSource = this.getValueWithMap(this.rootModel, "dataSource");
        Object database = this.getValueWithMap(dataSource, "database");
        List<Map<String, Object>> databases = (List<Map<String, Object>>) (database instanceof List ? database : Collections.singletonList(database));
        databases.stream()
                .peek(map -> map.put("type", RuleDataSourceDefinition.Type.database.name()))
                .forEach(map -> map.put("config", Collections.singletonMap("sql", map.get("sql"))));
        return databases;
    }

    @Override
    protected List<Map<String, Object>> doBuildRuleDefinitions(RuleGroupDefinition ruleGroupDefinition) {
        Map<String, Object> ruleSet = this.getValueWithMap(this.rootModel, "ruleSet");
        Object rule = this.getValueWithMap(ruleSet, "rule");
        return (List<Map<String, Object>>) (rule instanceof List ? rule : Collections.singletonList(rule));
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
