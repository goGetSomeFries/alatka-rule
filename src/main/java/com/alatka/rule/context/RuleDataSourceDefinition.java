package com.alatka.rule.context;

import java.util.Map;

/**
 * 规则外部数据源定义类
 *
 * @author whocares
 */
public class RuleDataSourceDefinition extends AbstractDefinition {

    public static RuleDataSourceDefinition DEFAULT_INSTANCE;

    static {
        RuleDataSourceDefinition instance = new RuleDataSourceDefinition();
        instance.setType(Type.current);
        instance.setName("默认数据源");
        DEFAULT_INSTANCE = instance;
    }

    /**
     * 类型
     */
    private Type type;

    /**
     * 数据范围
     *
     * @see Scope#global
     * @see Scope#request
     * @see Scope#rule
     */
    private Scope scope;

    /**
     * 返回数据类型
     *
     * @see ResultType#list
     * @see ResultType#single
     */
    private ResultType resultType;

    /**
     * 自定义参数
     */
    private Map<String, Object> config;

    public RuleDataSourceDefinition() {
        super(null);
    }

    public RuleDataSourceDefinition(String id) {
        super(id);
    }

    public enum Type {

        current, database, redis, elasticsearch;
    }

    public enum Scope {
        /**
         * 全局范围
         */
        global,
        /**
         * 请求数据范围
         */
        request,
        /**
         * 规则范围
         */
        rule
    }

    public enum ResultType {
        /**
         * 单笔
         */
        single,
        /**
         * 集合
         */
        list
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }
}
