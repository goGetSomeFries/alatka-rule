package com.alatka.rule.context;

import java.util.Map;

public class RuleDataSourceDefinition extends AbstractDefinition {

    public static RuleDataSourceDefinition DEFAULT_INSTANCE;

    static {
       RuleDataSourceDefinition instance = new RuleDataSourceDefinition();
       instance.setType(Type.current);
       instance.setDesc("默认数据源");
       DEFAULT_INSTANCE = instance;
    }

    private Type type;

    private Scope scope;

    private String desc;

    private ResultType resultType;

    private Map<String, Object> config;

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

    public enum ResultType {
        single, list
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

    public ResultType getResultType() {
        return resultType;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }
}
