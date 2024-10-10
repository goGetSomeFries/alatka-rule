package com.alatka.rule;

import com.alatka.rule.context.RuleDefinition;
import com.alatka.rule.context.RuleDefinitionContext;
import com.alatka.rule.context.RuleUnitDefinition;
import com.alatka.rule.parser.DefaultRuleParser;
import com.alatka.rule.parser.RuleParser;
import com.alatka.rule.util.JsonUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    private AviatorEvaluatorInstance aviatorEvaluatorInstance;

    public Main() {
        this.aviatorEvaluatorInstance = AviatorEvaluator.getInstance();
    }


    public <T> List<String> execute(String groupName, T object) {
        List<RuleDefinition> rules = RuleDefinitionContext.getInstance().getRuleGroup(groupName);
        Map<String, Object> params = JsonUtil.objectToMap(object);
        List<String> result = new ArrayList<>(0);

        for (RuleDefinition ruleDefinition : rules) {
            this.doExecute(ruleDefinition, ruleDefinition.getRuleUnitDefinition(), params, result);
        }
        return result;
    }

    private void doExecute(RuleDefinition ruleDefinition,
                           RuleUnitDefinition ruleUnitDefinition,
                           Map<String, Object> params,
                           List<String> result) {
        RuleParser ruleParser = new DefaultRuleParser();
        Map<String, Object> env = ruleParser.getEnv(params, true);

        Expression exp = aviatorEvaluatorInstance.compile(ruleDefinition.getId(), ruleUnitDefinition.getExpression(), true);
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
