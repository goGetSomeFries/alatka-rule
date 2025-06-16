package com.alatka.rule.core.context;

import java.util.Map;

/**
 * 规则定义类
 *
 * @author whocares
 */
public class RuleDefinition extends AbstractRuleDefinition {

    public RuleDefinition() {
        super(null);
    }

    /**
     * 规则描述
     */
    private String desc;

    /**
     * 规则优先级
     */
    private int priority;

    /**
     * 规则评分
     */
    private int score;

    /**
     * 扩展字段
     */
    private Map<String, Object> extended;

    /**
     * {@link RuleUnitDefinition}引用
     */
    private RuleUnitDefinition ruleUnitDefinition;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Map<String, Object> getExtended() {
        return extended;
    }

    public void setExtended(Map<String, Object> extended) {
        this.extended = extended;
    }

    public RuleUnitDefinition getRuleUnitDefinition() {
        return ruleUnitDefinition;
    }

    public void setRuleUnitDefinition(RuleUnitDefinition ruleUnitDefinition) {
        this.ruleUnitDefinition = ruleUnitDefinition;
    }

    @Override
    public String toString() {
        return "{id='" + getId() + "', name='" + getName() + "', priority='" + priority + "', score='" + score + "'}";
    }
}
