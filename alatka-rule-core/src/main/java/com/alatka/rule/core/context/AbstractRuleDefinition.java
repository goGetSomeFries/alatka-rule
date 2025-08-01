package com.alatka.rule.core.context;

import java.util.Objects;

/**
 * 规则抽象类
 *
 * @author whocares
 */
public abstract class AbstractRuleDefinition {

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

    public AbstractRuleDefinition(String id) {
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
        AbstractRuleDefinition that = (AbstractRuleDefinition) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
