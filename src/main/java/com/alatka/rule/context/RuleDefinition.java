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

    private String desc;

    private int priority;

    private int score;

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
        return "{id='" + getId() + "', " + "name='" + getName() + "', priority='" + priority + "', score='" + score + "'}";
    }
}
