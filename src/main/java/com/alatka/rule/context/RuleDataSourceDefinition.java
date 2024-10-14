package com.alatka.rule.context;

import java.util.Map;

public class RuleDataSourceDefinition extends AbstractDefinition {

    private Type type;

    private Scope scope;

    private String desc;

    private Map<String, Object> params;

    public RuleDataSourceDefinition() {
        super(null);
    }

    public RuleDataSourceDefinition(String id) {
        super(id);
    }

    public enum Type {

        current, database;
    }

    public enum Scope {
        global, data, rule
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
