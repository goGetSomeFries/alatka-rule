package com.alatka.rule.core.context;

/**
 * 规则组定义类
 *
 * @author whocares
 */
public class RuleGroupDefinition extends AbstractDefinition {

    public RuleGroupDefinition() {
        super(null);
    }

    public RuleGroupDefinition(String id) {
        super(id);
    }

    /**
     * 命中规则类型
     *
     * @see Type#all
     * @see Type#once
     * @see Type#priority_all
     * @see Type#priority_once
     */
    private Type type;

    /**
     * 规则黑白名单
     */
    private RuleListDefinition ruleListDefinition;


    public enum Type {
        /**
         * 所有规则全部执行<br><br>
         * A, B, C_, D, E_, F_, G --> C_, E_, F_
         */
        all,
        /**
         * 规则命中即停止<br><br>
         * A, B, C_, D, E_, F_, G --> C_
         */
        once,
        /**
         * 同一优先级内部所有规则全部执行；不同优先级命中即停止<br><br>
         * (A, B), (C_, D, E_), (F_, G) --> C_, E_
         */
        priority_all,
        /**
         * 同一优先级内部规则命中即停止；不同优先级全部执行<br><br>
         * (A, B), (C_, D, E_), (F_, G) --> C_, F_
         */
        priority_once
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public RuleListDefinition getRuleListDefinition() {
        return ruleListDefinition;
    }

    public void setRuleListDefinition(RuleListDefinition ruleListDefinition) {
        this.ruleListDefinition = ruleListDefinition;
    }

    @Override
    public String toString() {
        return "RuleGroupDefinition{id='" + getId() + "', " + "name='" + getName() + "'}";
    }
}
