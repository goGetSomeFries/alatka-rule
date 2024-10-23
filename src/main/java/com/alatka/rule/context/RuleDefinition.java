package com.alatka.rule.context;

/**
 * 规则定义类
 *
 * @author whocares
 */
public class RuleDefinition extends AbstractDefinition {

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
