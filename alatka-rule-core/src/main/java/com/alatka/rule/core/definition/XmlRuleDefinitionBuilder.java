package com.alatka.rule.core.definition;

import com.alatka.rule.core.context.RuleDataSourceDefinition;
import com.alatka.rule.core.context.RuleGroupDefinition;
import com.alatka.rule.core.support.FileWrapper;
import com.alatka.rule.core.util.XmlUtil;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    protected Map<String, Object> initRootModel(FileWrapper source) {
        Map<String, Object> map = XmlUtil.getMap(source, Object.class);
        return this.getValueWithMap(map, "alatka-rule");
    }

    @Override
    @SuppressWarnings("unchecked")
    protected List<Map<String, Object>> doBuildRuleDataSourceDefinitions(RuleGroupDefinition ruleGroupDefinition) {
        Map<String, Object> dataSource = this.getValueWithMap(this.rootModel, "dataSource");
        if (dataSource == null) {
            return Collections.EMPTY_LIST;
        }

        List<Map<String, Object>> databaseList =
                doBuildRuleDataSourceItems(dataSource, RuleDataSourceDefinition.Type.database, "sql", "resultType", "resultClass");
        List<Map<String, Object>> redisList =
                doBuildRuleDataSourceItems(dataSource, RuleDataSourceDefinition.Type.redis, "type", "key", "hashKey", "setKey");

        List<Map<String, Object>> result = new ArrayList<>();
        result.addAll(databaseList);
        result.addAll(redisList);
        return result;
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> doBuildRuleDataSourceItems(Map<String, Object> dataSource,
                                                                 RuleDataSourceDefinition.Type type,
                                                                 String... keys) {
        Object item = this.getValueWithMap(dataSource, type.name(), Collections.EMPTY_LIST);
        List<Map<String, Object>> list = (List<Map<String, Object>>) (item instanceof List ? item : Collections.singletonList(item));
        list.stream()
                .peek(map -> {
                    Map<String, Object> config = new HashMap<>();
                    Stream.of(keys).forEach(key -> config.put(key, map.remove(key)));
                    map.put("config", config);
                })
                .forEach(map -> map.put("type", type.name()));
        return list;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected List<Map<String, Object>> doBuildRuleParamDefinitions(RuleGroupDefinition ruleGroupDefinition) {
        Map<String, Object> params = this.getValueWithMap(this.rootModel, "params", Collections.EMPTY_MAP);
        Object param = this.getValueWithMap(params, "param", Collections.emptyList());
        return (List<Map<String, Object>>) (param instanceof List ? param : Collections.singletonList(param));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Map<String, Object> doBuildRuleListDefinition(RuleGroupDefinition ruleGroupDefinition) {
        return this.getValueWithMap(this.rootModel, "filterList", Collections.EMPTY_MAP);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected List<Map<String, Object>> doBuildRuleDefinitions(RuleGroupDefinition ruleGroupDefinition) {
        Map<String, Object> ruleSet = this.getValueWithMap(this.rootModel, "ruleSet", Collections.EMPTY_MAP);
        Object rule = this.getValueWithMap(ruleSet, "rule", Collections.emptyList());
        return (List<Map<String, Object>>) (rule instanceof List ? rule : Collections.singletonList(rule));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Map<String, Object> buildRuleExtendedProperties(Map<String, Object> map) {
        Object object = this.getValueWithMap(map, "extended", Collections.emptyList());
        List<Map<String, Object>> list =
                (List<Map<String, Object>>) (object instanceof List ? object : Collections.singletonList(object));
        return list.stream()
                .collect(Collectors.toMap(e -> getValueWithMapOrThrow(e, "key"),
                        e -> getValueWithMapOrThrow(e, "value")));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected List<Map<String, Object>> doBuildRuleUnitDefinitions(Map<String, Object> map) {
        Object object = this.getValueWithMap(map, "unit", Collections.emptyList());
        return (List<Map<String, Object>>) (object instanceof List ? object : Collections.singletonList(object));
    }

    @Override
    protected String[] suffix() {
        return new String[]{".xml"};
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <S> S getValueWithMap(Map<String, Object> map, String key) {
        Object object = map.get(key);
        return (S) convert(object);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <S> S getValueWithMap(Map<String, Object> map, String key, S defaultValue) {
        Object object = map.getOrDefault(key, defaultValue);
        return (S) convert(object);
    }

    @Override
    @SuppressWarnings("unchecked")
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
