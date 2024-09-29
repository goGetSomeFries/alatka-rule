package com.alatka.rule;

import com.alatka.rule.context.RuleContext;
import com.alatka.rule.context.RuleDefinition;
import com.alatka.rule.parser.DefaultRuleParser;
import com.alatka.rule.parser.RuleParser;
import com.alatka.rule.util.JsonUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    public <T> void execute(T object) {

        List<RuleDefinition> ruleGroup = RuleContext.getInstance().getRuleGroup("");
        Map<String, Object> params = JsonUtil.objectToMap(object);
        List<String> result = new ArrayList<>();

        for (RuleDefinition ruleDefinition : ruleGroup) {
            RuleParser ruleParser = new DefaultRuleParser();
            Map<String, Object> env = ruleParser.getEnv(params, true);
            boolean flag = doExecute("", "", env);
            if (!flag) {
                continue;
            } else if (ruleDefinition.getNext() != null) {
                result.add(ruleDefinition.getId());
            } else {

            }
        }

    }

    private void test(List<String> result, RuleDefinition ruleDefinition, Map<String, Object> params) {
        RuleParser ruleParser = new DefaultRuleParser();
        Map<String, Object> env = ruleParser.getEnv(params, true);
        boolean flag = doExecute("", "", env);
        if (!flag) {
            return;
        }
        if (ruleDefinition.getNext() != null) {
            result.add(ruleDefinition.getId());
        } else {

        }
    }

    private boolean doExecute(String key, String expression, Map<String, Object> env) {
        Expression exp = AviatorEvaluator.getInstance().compile(key, expression, true);
        return (boolean) exp.execute(env);
    }
}
