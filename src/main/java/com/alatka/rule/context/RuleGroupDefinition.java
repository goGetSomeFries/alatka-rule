package com.alatka.rule.context;

import java.util.Objects;

public class RuleGroupDefinition {

    public RuleGroupDefinition() {
    }

    public RuleGroupDefinition(String id) {
        this.id = id;
    }

    private String id;

    private String desc;

    private Type type;

    private boolean enabled = true;

    public enum Type {
        greedy, short_circle, level_greedy, level_short_circle
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RuleGroupDefinition that = (RuleGroupDefinition) o;
        return Objects.equals(this.id, that.id);
    }
}
