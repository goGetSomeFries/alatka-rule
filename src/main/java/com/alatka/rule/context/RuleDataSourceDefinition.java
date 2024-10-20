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
     * 自定义参数
     */
    private Map<String, String> config;

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

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }
}
