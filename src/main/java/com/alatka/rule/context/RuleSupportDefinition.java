package com.alatka.rule.context;

public class RuleSupportDefinition {

    private String id;

    private String sql;

    private Scope scope;

    public enum Scope {
        global, data, rule
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }
}
