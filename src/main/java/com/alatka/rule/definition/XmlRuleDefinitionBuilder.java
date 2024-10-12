package com.alatka.rule.definition;

import com.alatka.rule.util.XmlUtil;

import java.nio.file.Path;
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
    protected String rulesKey() {
        return "rule";
    }

    @Override
    protected String ruleUnitsKey() {
        return "unit";
    }

    @Override
    protected String[] suffix() {
        return new String[]{".xml"};
    }

}
