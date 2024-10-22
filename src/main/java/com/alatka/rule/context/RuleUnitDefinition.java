package com.alatka.rule.context;

/**
 * 规则单元定义类
 *
 * @author whocares
 */
public class RuleUnitDefinition {

    /**
     * 序号
     */
    private int index;
    /**
     * 是否可用
     */
    private boolean enabled = true;

    /**
     * {@link RuleDataSourceDefinition}引用
     */
    private RuleDataSourceDefinition dataSourceRef;

    /**
     * 规则表达式
     */
    private String expression;

    /**
     * 下一个{@link RuleUnitDefinition}实例，null则代表没有
     */
    private RuleUnitDefinition next;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public RuleDataSourceDefinition getDataSourceRef() {
        return dataSourceRef;
    }

    public void setDataSourceRef(RuleDataSourceDefinition dataSourceRef) {
        this.dataSourceRef = dataSourceRef;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public RuleUnitDefinition getNext() {
        return next;
    }

    public void setNext(RuleUnitDefinition next) {
        this.next = next;
    }
}
