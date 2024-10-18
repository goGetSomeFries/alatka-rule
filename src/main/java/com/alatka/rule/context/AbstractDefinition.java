package com.alatka.rule.context;

import java.util.Objects;

public abstract class AbstractDefinition {

    /**
     * 唯一标识
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 是否可用
     */
    private boolean enabled = true;

    public AbstractDefinition(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        AbstractDefinition that = (AbstractDefinition) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
