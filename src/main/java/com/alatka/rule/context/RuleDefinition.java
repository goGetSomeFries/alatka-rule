package com.alatka.rule.context;

public class RuleDefinition {

    private String id;

    private boolean enabled;

    private Type type;

    private String expression;

    private RuleDefinition next;

    public enum Type {

        default_("default"), database("database");

        private String key;

        Type(String key) {
            this.key = key;
        }
    }

    public enum Scope {
        global, data, rule
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public RuleDefinition getNext() {
        return next;
    }

    public void setNext(RuleDefinition next) {
        this.next = next;
    }
}
