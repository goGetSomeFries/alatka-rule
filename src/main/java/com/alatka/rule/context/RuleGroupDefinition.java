package com.alatka.rule.context;

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

    private String desc;

    /**
     * 命中规则类型
     *
     * @see Type#greedy
     * @see Type#short_circle
     * @see Type#priority_greedy
     * @see Type#priority_short_circle
     */
    private Type type;


    public enum Type {
        /**
         * 所有规则全部执行<br><br>
         * A, B, C_, D, E_, F_, G --> C_, E_, F_
         */
        greedy,
        /**
         * 规则命中即停止<br><br>
         * A, B, C_, D, E_, F_, G --> C_
         */
        short_circle,
        /**
         * 同一优先级内部所有规则全部执行；不同优先级命中即停止<br><br>
         * (A, B), (C_, D, E_), (F_, G) --> C_, E_
         */
        priority_greedy,
        /**
         * 同一优先级内部规则命中即停止；不同优先级全部执行<br><br>
         * (A, B), (C_, D, E_), (F_, G) --> C_, F_
         */
        priority_short_circle
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

    @Override
    public String toString() {
        return "RuleGroupDefinition{id='" + getId() + "', " + "desc='" + desc + "'}";
    }
}
