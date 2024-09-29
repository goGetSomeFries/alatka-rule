package com.alatka.rule.context;

public class RuleDefinition {

    private String id;

    private RuleDefinition next;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RuleDefinition getNext() {
        return next;
    }

    public void setNext(RuleDefinition next) {
        this.next = next;
    }
}
