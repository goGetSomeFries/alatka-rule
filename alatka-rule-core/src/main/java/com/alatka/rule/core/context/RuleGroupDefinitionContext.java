package com.alatka.rule.core.context;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 规则组Context，维护{@link RuleGroupDefinition} -> {@link RuleDefinition}/{@link RuleParamDefinition}集合
 *
 * @author whocares
 */
public class RuleGroupDefinitionContext {

    /**
     * 蓝绿发布标识
     */
    private static volatile boolean SWITCH_FLAG = true;

    private final Map<RuleGroupDefinition, List<RuleDefinition>> ruleDefinitionsMap = new HashMap<>();

    private final Map<RuleGroupDefinition, List<RuleParamDefinition>> ruleParamDefinitionsMap = new HashMap<>();

    private final AviatorEvaluatorInstance aviatorEvaluatorInstance;

    private RuleGroupDefinitionContext() {
        this.aviatorEvaluatorInstance = AviatorEvaluator.newInstance();
    }

    public AviatorEvaluatorInstance getAviatorEvaluatorInstance() {
        return this.aviatorEvaluatorInstance;
    }

    public RuleGroupDefinition getRuleGroupDefinition(String ruleGroupName) {
        RuleGroupDefinition ruleGroupDefinition = new RuleGroupDefinition(ruleGroupName);
        return this.ruleDefinitionsMap.keySet()
                .stream()
                .filter(ruleGroupDefinition::equals)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ruleGroupName + " not exists"));
    }

    /**
     * 根据规则组名称获取规则集合
     *
     * @param ruleGroupName 规则组名称
     * @return 规则集合
     */
    public List<RuleDefinition> getRuleDefinitions(String ruleGroupName) {
        RuleGroupDefinition ruleGroupDefinition = new RuleGroupDefinition(ruleGroupName);
        List<RuleDefinition> ruleDefinitions = this.ruleDefinitionsMap.get(ruleGroupDefinition);
        if (ruleDefinitions == null) {
            throw new IllegalArgumentException(ruleGroupName + " not exists");
        }
        return ruleDefinitions;
    }

    /**
     * 根据规则组名称获取规则入参处理集合
     *
     * @param ruleGroupName 规则组名称
     * @return 规则入参处理集合
     */
    public List<RuleParamDefinition> getRuleParamDefinitions(String ruleGroupName) {
        RuleGroupDefinition ruleGroupDefinition = new RuleGroupDefinition(ruleGroupName);
        List<RuleParamDefinition> ruleParamDefinitions = this.ruleParamDefinitionsMap.get(ruleGroupDefinition);
        if (ruleParamDefinitions == null) {
            throw new IllegalArgumentException(ruleGroupName + " not exists");
        }
        return ruleParamDefinitions;
    }

    /**
     * 初始化规则组和规则集合映射
     *
     * @param ruleGroupDefinition 规则组
     * @param ruleDefinitions     规则集合
     */
    public void initRuleDefinitions(RuleGroupDefinition ruleGroupDefinition, List<RuleDefinition> ruleDefinitions) {
        if (this.ruleDefinitionsMap.containsKey(ruleGroupDefinition)) {
            throw new IllegalArgumentException(ruleGroupDefinition + " already exists");
        }
        this.ruleDefinitionsMap.put(ruleGroupDefinition, ruleDefinitions);
    }

    /**
     * 初始化规则组和规则入参处理集合映射
     *
     * @param ruleGroupDefinition  规则组
     * @param ruleParamDefinitions 规则入参处理集合
     */
    public void initRuleParamDefinitions(RuleGroupDefinition ruleGroupDefinition, List<RuleParamDefinition> ruleParamDefinitions) {
        if (ruleParamDefinitionsMap.containsKey(ruleGroupDefinition)) {
            throw new IllegalArgumentException(ruleGroupDefinition + " already exists");
        }
        this.ruleParamDefinitionsMap.put(ruleGroupDefinition, ruleParamDefinitions);
    }

    /**
     * 清除
     */
    public void reset() {
        this.ruleDefinitionsMap.clear();
        this.ruleParamDefinitionsMap.clear();
        this.aviatorEvaluatorInstance.clearExpressionCache();
    }

    /**
     * 蓝绿发布切换
     */
    public static void toggle() {
        SWITCH_FLAG = !SWITCH_FLAG;
    }

    /**
     * 获取Context实例，用于蓝绿发布
     *
     * @param mode 读写模式，代表调用该方法得到Context实例的目的是配置（写）还是使用（读）<br>
     *             true:读模式 false:写模式
     * @return {@link RuleGroupDefinitionContext}实例
     */
    public static RuleGroupDefinitionContext getInstance(boolean mode) {
        return mode == SWITCH_FLAG ? Inner1.INSTANCE : Inner2.INSTANCE;
    }

    private static class Inner1 {
        private static final RuleGroupDefinitionContext INSTANCE = new RuleGroupDefinitionContext();
    }

    private static class Inner2 {
        private static final RuleGroupDefinitionContext INSTANCE = new RuleGroupDefinitionContext();
    }
}
