package com.alatka.rule.core;

import com.alatka.rule.core.context.*;
import com.alatka.rule.core.datasource.ExternalDataSource;
import com.alatka.rule.core.datasource.ExternalDataSourceFactory;
import com.alatka.rule.core.support.InnerConstant;
import com.alatka.rule.core.util.JsonUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 规则引擎
 *
 * @author whocares
 */
public class RuleEngine {

    private final Logger logger = LoggerFactory.getLogger(RuleEngine.class);

    /**
     * 获取{@link AviatorEvaluatorInstance}
     *
     * @return {@link AviatorEvaluatorInstance}
     */
    public AviatorEvaluatorInstance getAviatorEvaluatorInstance() {
        RuleGroupDefinitionContext definitionContext = RuleGroupDefinitionContext.getInstance(true);
        return definitionContext.getAviatorEvaluatorInstance();
    }

    /**
     * 验证表达式是否合法
     *
     * @param expression 验证表达式
     */
    public void validate(String expression) {
        AviatorEvaluator.validate(expression);
    }

    /**
     * 执行规则，得到命中规则集合
     *
     * @param ruleGroupName 规则组名称
     * @param param         规则入参
     * @return {@link RuleDefinition}集合
     */
    public List<RuleDefinition> execute(String ruleGroupName, Object param) {
        // 定义命中规则结果集
        List<RuleDefinition> result = new ArrayList<>(0);

        RuleGroupDefinitionContext definitionContext = RuleGroupDefinitionContext.getInstance(true);
        AviatorEvaluatorInstance aviatorEvaluatorInstance = definitionContext.getAviatorEvaluatorInstance();
        RuleGroupDefinition ruleGroupDefinition = definitionContext.getRuleGroupDefinition(ruleGroupName);

        // 入参预处理
        List<RuleParamDefinition> ruleParamDefinitions = definitionContext.getRuleParamDefinitions(ruleGroupName);
        Map<String, Object> paramContext = this.initParamContext(aviatorEvaluatorInstance, param, ruleParamDefinitions);

        // global范围外部数据存储
        ConcurrentHashMap<String, Object> globalScopeData = definitionContext.getGlobalScopeData(ruleGroupName);

        // 黑白名单过滤
        if (this.listFilter(aviatorEvaluatorInstance, paramContext, globalScopeData, ruleGroupDefinition.getRuleListDefinition())) {
            return result;
        }

        // 初始化元数据
        paramContext.put(InnerConstant.META_HIT_RESULT, result);
        paramContext.put(InnerConstant.META_EXIT_FLAG, false);

        List<RuleDefinition> ruleDefinitions = definitionContext.getRuleDefinitions(ruleGroupName);
        RuleDefinition theOne = null;

        // 规则集合判断
        outerLoop:
        for (RuleDefinition ruleDefinition : ruleDefinitions) {
            switch (ruleGroupDefinition.getType()) {
                case all:
                    // 所有规则全部执行
                    break;
                case once:
                    // 规则命中即停止
                    if (theOne != null) {
                        break outerLoop;
                    }
                    break;
                case priority_all:
                    // 同一优先级内部所有规则全部执行；不同优先级命中即停止
                    if (theOne != null && theOne.getPriority() != ruleDefinition.getPriority()) {
                        break outerLoop;
                    }
                    break;
                case priority_once:
                    // 同一优先级内部规则命中即停止；不同优先级全部执行
                    if (theOne != null && theOne.getPriority() == ruleDefinition.getPriority()) {
                        continue;
                    }
                    break;
                case customize:
                    if ((boolean) paramContext.get(InnerConstant.META_EXIT_FLAG)) {
                        break outerLoop;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("error type: " + ruleGroupDefinition.getType());
            }

            // 规则判断
            boolean hit = this.doExecute(aviatorEvaluatorInstance, ruleDefinition.getRuleUnitDefinition(), paramContext, globalScopeData);

            if (hit) {
                result.add(ruleDefinition);
                theOne = ruleDefinition;
            }
        }

        this.logger.debug("execute: {}; params: {}; result: {}", ruleGroupDefinition, paramContext, result);
        return result;
    }

    /**
     * 规则入参预处理
     *
     * @param aviatorEvaluatorInstance aviator实例
     * @param param                    规则入参
     * @param list                     {@link RuleParamDefinition}集合
     * @return 处理后入参
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> initParamContext(AviatorEvaluatorInstance aviatorEvaluatorInstance,
                                                 Object param, List<RuleParamDefinition> list) {
        Map<String, Object> paramContext =
                param instanceof Map ? new HashMap<>((Map<String, Object>) param) : JsonUtil.objectToMap(param);

        list.forEach(ruleParamDefinition -> {
            Object value = this.calculateExpression(aviatorEvaluatorInstance, ruleParamDefinition.getExpression(), paramContext);
            if (value != null) {
                paramContext.put(ruleParamDefinition.getId(), value);
            }
        });
        return paramContext;
    }

    /**
     * 黑白名单过滤
     *
     * @param aviatorEvaluatorInstance aviator实例
     * @param paramContext             规则入参
     * @param ruleListDefinition       规则黑白名单
     * @return true:过滤/false:不过滤
     */
    private boolean listFilter(AviatorEvaluatorInstance aviatorEvaluatorInstance, Map<String, Object> paramContext,
                               Map<String, Object> globalScopeData, RuleListDefinition ruleListDefinition) {
        if (ruleListDefinition != null && ruleListDefinition.isEnabled()) {
            boolean hit = this.doListFilter(aviatorEvaluatorInstance, ruleListDefinition.getRuleUnitDefinition(), paramContext, globalScopeData);
            return (RuleListDefinition.Type.blackList == ruleListDefinition.getType()) == hit;
        }
        return false;
    }

    /**
     * 递归判断{@link RuleUnitDefinition}规则单元，任意命中则其归属黑白名单命中
     *
     * @param aviatorEvaluatorInstance aviator实例
     * @param ruleUnitDefinition       规则单元
     * @param paramContext             规则入参
     * @return 是否命中黑白名单
     */
    private boolean doListFilter(AviatorEvaluatorInstance aviatorEvaluatorInstance, RuleUnitDefinition ruleUnitDefinition,
                                 Map<String, Object> paramContext, Map<String, Object> globalScopeData) {
        Map<String, Object> env = this.extendParamContext(ruleUnitDefinition.getDataSourceRef(), paramContext, globalScopeData);
        boolean hit = this.calculateExpression(aviatorEvaluatorInstance, ruleUnitDefinition.getExpression(), env);

        if (hit) {
            // 命中规则单元，结束当前规则判断
            return true;
        }
        if (ruleUnitDefinition.getNext() == null) {
            // 未命中规则单元，无后续规则单元，则当前规则未命中
            return false;
        }
        // 未命中规则单元，有后续规则单元，则继续执行后续规则单元
        return this.doListFilter(aviatorEvaluatorInstance, ruleUnitDefinition.getNext(), env, globalScopeData);
    }

    /**
     * 递归判断{@link RuleUnitDefinition}规则单元，全部命中则其归属{@link RuleDefinition}规则命中
     *
     * @param aviatorEvaluatorInstance aviator实例
     * @param ruleUnitDefinition       规则单元
     * @param paramContext             规则入参
     * @return 是否命中
     */
    private boolean doExecute(AviatorEvaluatorInstance aviatorEvaluatorInstance, RuleUnitDefinition ruleUnitDefinition,
                              Map<String, Object> paramContext, Map<String, Object> globalScopeData) {
        Map<String, Object> env = this.extendParamContext(ruleUnitDefinition.getDataSourceRef(), paramContext, globalScopeData);
        boolean hit = this.calculateExpression(aviatorEvaluatorInstance, ruleUnitDefinition.getExpression(), env);

        if (!hit) {
            // 未命中规则单元，结束当前规则判断
            return false;
        }
        if (ruleUnitDefinition.getNext() == null) {
            // 命中规则单元，无后续规则单元，则当前规则命中
            return true;
        } else {
            // 命中规则单元，有后续规则单元，则继续执行后续规则单元
            return this.doExecute(aviatorEvaluatorInstance, ruleUnitDefinition.getNext(), env, globalScopeData);
        }
    }

    /**
     * 通过外部数据源构建请求上下文
     *
     * @param ruleDataSourceDefinition {@link RuleDataSourceDefinition}
     * @param paramContext             请求上下文
     * @return 结果上下文
     */
    private Map<String, Object> extendParamContext(RuleDataSourceDefinition ruleDataSourceDefinition,
                                                   Map<String, Object> paramContext, Map<String, Object> globalScopeData) {
        ExternalDataSourceFactory factory = ExternalDataSourceFactory.getInstance();
        ExternalDataSource externalDataSource = factory.getExternalDataSource(ruleDataSourceDefinition.getType());
        return externalDataSource.buildContext(ruleDataSourceDefinition, paramContext, globalScopeData);
    }

    /**
     * 执行表达式
     *
     * @param aviatorEvaluatorInstance aviator实例
     * @param expression               表达式
     * @param env                      表达式入参
     * @param <T>                      返回值类型
     * @return 表达式执行结果
     */
    @SuppressWarnings("unchecked")
    private <T> T calculateExpression(AviatorEvaluatorInstance aviatorEvaluatorInstance, String expression, Map<String, Object> env) {
        Expression exp = aviatorEvaluatorInstance.compile(Utils.md5sum(expression), expression, true);
        return (T) exp.execute(env);
    }

}
