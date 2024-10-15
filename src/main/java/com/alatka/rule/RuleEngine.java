package com.alatka.rule;

import com.alatka.rule.context.RuleDefinition;
import com.alatka.rule.context.RuleGroupDefinition;
import com.alatka.rule.context.RuleGroupDefinitionContext;
import com.alatka.rule.context.RuleUnitDefinition;
import com.alatka.rule.datasource.DataSourceBuilder;
import com.alatka.rule.util.JsonUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RuleEngine {

    private final AviatorEvaluatorInstance aviatorEvaluatorInstance;

    private final String ruleGroupName;

    public RuleEngine(String ruleGroupName) {
        this.ruleGroupName = ruleGroupName;
        this.aviatorEvaluatorInstance = AviatorEvaluator.getInstance();
    }


    public List<String> execute(Object input) {
        RuleGroupDefinitionContext context = RuleGroupDefinitionContext.getInstance();
        List<RuleDefinition> ruleDefinitions = context.getRuleDefinitions(ruleGroupName);
        RuleGroupDefinition ruleGroupDefinition = context.getRuleGroupDefinition(ruleGroupName);
        RuleGroupDefinition.Type type = ruleGroupDefinition.getType();

        Map<String, Object> params = JsonUtil.objectToMap(input);
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

            this.doExecute(ruleDefinition, ruleDefinition.getRuleUnitDefinition(), params, result);

            if (result.size() > 0) {
                theOne = ruleDefinition;
            }
        }

        return result;
    }

    private void doExecute(RuleDefinition ruleDefinition, RuleUnitDefinition ruleUnitDefinition,
                           Map<String, Object> params, List<String> result) {
        // TODO 规则在线发布
        String cacheKey = ruleDefinition + ruleUnitDefinition.toString();
        Expression exp = aviatorEvaluatorInstance.compile(cacheKey, ruleUnitDefinition.getExpression(), true);
        // TODO
        DataSourceBuilder dataSourceBuilder = null;
        Map<String, Object> env = dataSourceBuilder.getContext(ruleUnitDefinition.getDataSourceRef(), params);
        boolean hit = (boolean) exp.execute(env);

        if (!hit) {
            return;
        }
        if (ruleUnitDefinition.getNext() == null) {
            result.add(ruleDefinition.getId());
        } else {
            this.doExecute(ruleDefinition, ruleUnitDefinition.getNext(), env, result);
        }
    }

}
