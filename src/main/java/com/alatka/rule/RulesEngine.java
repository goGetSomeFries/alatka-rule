package com.alatka.rule;

import com.alatka.rule.context.*;
import com.alatka.rule.datasource.DataSourceBuilder;
import com.alatka.rule.datasource.DataSourceBuilderFactory;
import com.alatka.rule.util.JsonUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 规则引擎
 *
 * @author whocares
 */
public class RulesEngine {

    private final AviatorEvaluatorInstance aviatorEvaluatorInstance;

    private final String ruleGroupName;

    public RulesEngine(String ruleGroupName) {
        this.ruleGroupName = ruleGroupName;
        this.aviatorEvaluatorInstance = AviatorEvaluator.getInstance();
    }

    public RulesEngine(String ruleGroupName, AviatorEvaluatorInstance aviatorEvaluatorInstance) {
        this.ruleGroupName = ruleGroupName;
        this.aviatorEvaluatorInstance = aviatorEvaluatorInstance;
    }

    /**
     * 执行规则，得到命中规则id集合
     *
     * @param param 规则入参
     * @return {@link RuleDefinition#id}集合
     */
    public List<String> execute(Object param) {
        RuleGroupDefinitionContext definitionContext = RuleGroupDefinitionContext.getInstance(true);
        List<RuleDefinition> ruleDefinitions = definitionContext.getRuleDefinitions(ruleGroupName);
        RuleGroupDefinition ruleGroupDefinition = definitionContext.getRuleGroupDefinition(ruleGroupName);
        RuleGroupDefinition.Type type = ruleGroupDefinition.getType();

        Map<String, Object> paramContext =
                param instanceof Map ? new HashMap<>((Map<String, Object>) param) : JsonUtil.objectToMap(param);
        List<String> result = new ArrayList<>(0);
        RuleDefinition theOne = null;

        outerLoop:
        for (RuleDefinition ruleDefinition : ruleDefinitions) {
            switch (type) {
                case greedy:
                    break;
                case short_circle:
                    if (theOne != null) {
                        break outerLoop;
                    }
                    break;
                case priority_greedy:
                    if (theOne != null && theOne.getPriority() != ruleDefinition.getPriority()) {
                        break outerLoop;
                    }
                    break;
                case priority_short_circle:
                    if (theOne != null && theOne.getPriority() == ruleDefinition.getPriority()) {
                        continue;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("error type:" + type);
            }

            this.doExecute(ruleDefinition, ruleDefinition.getRuleUnitDefinition(), paramContext, result);

            if (result.size() > 0) {
                theOne = ruleDefinition;
            }
        }

        return result;
    }

    /**
     * 递归判断{@link RuleUnitDefinition}规则单元，全部命中则其归属{@link RuleDefinition}规则命中
     *
     * @param ruleDefinition     规则
     * @param ruleUnitDefinition 规则单元
     * @param paramContext       规则入参
     * @param result             命中结果集
     */
    private void doExecute(RuleDefinition ruleDefinition, RuleUnitDefinition ruleUnitDefinition,
                           Map<String, Object> paramContext, List<String> result) {
        RuleDataSourceDefinition ruleDataSourceDefinition = ruleUnitDefinition.getDataSourceRef();
        DataSourceBuilder dataSourceBuilder =
                DataSourceBuilderFactory.getInstance().getDataSourceBuilder(ruleDataSourceDefinition.getType());
        Map<String, Object> env = dataSourceBuilder.buildContext(ruleDataSourceDefinition, paramContext);

        String cacheKey = Utils.md5sum(ruleDefinition + ruleUnitDefinition.toString());
        Expression exp = aviatorEvaluatorInstance.compile(cacheKey, ruleUnitDefinition.getExpression(), true);
        boolean hit = (boolean) exp.execute(env);

        if (!hit) {
            // 未命中规则单元，结束当前规则判断
            return;
        }
        if (ruleUnitDefinition.getNext() == null) {
            // 命中规则单元，无后续规则单元，则当前规则命中
            result.add(ruleDefinition.getId());
        } else {
            // 命中规则单元，有后续规则单元，则继续执行后续规则单元
            this.doExecute(ruleDefinition, ruleUnitDefinition.getNext(), env, result);
        }
    }

}
