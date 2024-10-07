package com.alatka.rule.definition;

import com.alatka.rule.util.XmlUtil;

import java.nio.file.Path;
import java.util.Map;

public class XmlRuleDefinitionBuilder extends FileRuleDefinitionBuilder {

    public XmlRuleDefinitionBuilder(String classpath) {
        super(classpath);
    }

    @Override
    protected Map<String, Object> doBuildRuleGroupDefinition(Path source) {
        return XmlUtil.getMap(source.toFile(), Object.class);
    }

    @Override
    protected String[] suffix() {
        return new String[]{".yaml", "yml"};
    }
}
