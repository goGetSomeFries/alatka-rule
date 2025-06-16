package com.alatka.rule.core.context;

/**
 * 规则入参处理类
 *
 * @author whocares
 */
public class RuleParamDefinition extends AbstractRuleDefinition {

    /**
     * 入参处理表达式
     */
    private String expression;

    public RuleParamDefinition() {
        super(null);
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
