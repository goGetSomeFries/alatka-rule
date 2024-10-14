package com.alatka.rule.context;

public class RuleDefinition extends AbstractDefinition {

    public RuleDefinition() {
        super(null);
    }

    private String desc;

    private String remark;

    private int priority;

    private RuleUnitDefinition ruleUnitDefinition;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public RuleUnitDefinition getRuleUnitDefinition() {
        return ruleUnitDefinition;
    }

    public void setRuleUnitDefinition(RuleUnitDefinition ruleUnitDefinition) {
        this.ruleUnitDefinition = ruleUnitDefinition;
    }
}
