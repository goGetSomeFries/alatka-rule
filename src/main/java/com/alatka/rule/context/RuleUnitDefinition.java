package com.alatka.rule.context;

public class RuleUnitDefinition {

    private boolean enabled = true;

    private String dataSourceId;

    private String expression;

    private RuleUnitDefinition next;


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
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
