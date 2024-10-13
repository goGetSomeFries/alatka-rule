package com.alatka.rule.context;

public class RuleUnitDefinition {

    private boolean enabled = true;

    private Type type;

    private String expression;

    private RuleUnitDefinition next;

    public enum Type {

        current, database;
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

    public RuleUnitDefinition getNext() {
        return next;
    }

    public void setNext(RuleUnitDefinition next) {
        this.next = next;
    }
}
