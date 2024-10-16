package com.alatka.rule.context;

public class RuleGroupDefinition extends AbstractDefinition {

    public RuleGroupDefinition() {
        super(null);
    }

    public RuleGroupDefinition(String id) {
        super(id);
    }

    private String desc;

    private Type type;


    public enum Type {
        /**
         * 所有规则全部执行<br><br>
         * A, B, C_, D, E_, F_, G --> C_, E_, F_
         */
        greedy,
        /**
         * 首笔规则触发即停止<br><br>
         * A, B, C_, D, E_, F_, G --> C_
         */
        short_circle,
        /**
         * 同一优先级内部所有规则全部执行；不同优先级触发即停止<br><br>
         * (A, B), (C_, D, E_), (F_, G) --> C_, E_
         */
        priority_greedy,
        /**
         * 同一优先级内部首笔规则触发即停止；不同优先级全部执行<br><br>
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
