package com.alatka.rule.context;

public class RuleDefinition {

    private String id;

    private boolean enabled;

    private String desc;

    private String remark;

    private RuleUnitDefinition ruleUnitDefinition;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

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

    public RuleUnitDefinition getRuleUnitDefinition() {
        return ruleUnitDefinition;
    }

    public void setRuleUnitDefinition(RuleUnitDefinition ruleUnitDefinition) {
        this.ruleUnitDefinition = ruleUnitDefinition;
    }
}
