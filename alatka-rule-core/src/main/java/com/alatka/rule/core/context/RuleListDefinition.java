package com.alatka.rule.core.context;


/**
 * 规则黑白名单定义类
 *
 * @author whocares
 */
public class RuleListDefinition extends AbstractRuleDefinition {

    /**
     * @see Type#blackList
     * @see Type#whiteList
     */
    private Type type;

    /**
     * {@link RuleUnitDefinition}引用
     */
    private RuleUnitDefinition ruleUnitDefinition;

    public enum Type {
        blackList, whiteList
    }

    public RuleListDefinition() {
        super(null);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public RuleUnitDefinition getRuleUnitDefinition() {
        return ruleUnitDefinition;
    }

    public void setRuleUnitDefinition(RuleUnitDefinition ruleUnitDefinition) {
        this.ruleUnitDefinition = ruleUnitDefinition;
    }
}
