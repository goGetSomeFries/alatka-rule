package com.alatka.rule.core.definition;

import com.alatka.rule.core.context.*;
import com.alatka.rule.core.util.FileUtil;
import com.googlecode.aviator.AviatorEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * {@link RuleDefinitionBuilder}抽象类，实现{@link #build()}、{@link #refresh()}、{@link #fallback()}方法；
 * 抽象{@link RuleGroupDefinition}、{@link RuleDataSourceDefinition}、{@link RuleDefinition}、{@link RuleUnitDefinition}构建方法
 *
 * @param <T> 配置源类型
 * @author whocares
 */
public abstract class AbstractRuleDefinitionBuilder<T> implements RuleDefinitionBuilder {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 全局加锁，保证{@link #build()} {@link #refresh()} {@link #fallback()}串行执行
     */
    private static final Lock lock = new ReentrantLock(true);

    /**
     * 外部数据源映射，属于构建中间状态，构建结束后清除
     */
    private Map<String, RuleDataSourceDefinition> mapping;

    @Override
    public void build() {
        try {
            lock.lock();
            this.doBuild();
            this.logger.info("********* 规则配置build完成 *********");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void refresh() {
        try {
            lock.lock();
            this.doBuild();
            this.logger.info("********* 规则配置refresh完成 *********");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void fallback() {
        try {
            lock.lock();
            RuleGroupDefinitionContext.toggle();
            this.logger.info("********* 规则配置回退上一版本 *********");
        } finally {
            lock.unlock();
        }
    }

    private void doBuild() {
        RuleGroupDefinitionContext context = RuleGroupDefinitionContext.getInstance(false);
        context.reset();

        this.getSources().stream()
                .peek(this::preProcess)
                .map(this::buildRuleGroupDefinition)
                .filter(RuleGroupDefinition::isEnabled)
                .peek(ruleGroupDefinition -> this.logger.info("build {}", ruleGroupDefinition))
                .peek(ruleGroupDefinition -> this.mapping = this.buildRuleDataSourceDefinitionMap(ruleGroupDefinition))
                .peek(ruleGroupDefinition -> {
                    // 规则入参预处理
                    List<RuleParamDefinition> ruleParamDefinitions = this.buildRuleParamDefinitions(ruleGroupDefinition);
                    context.initRuleParamDefinitions(ruleGroupDefinition, ruleParamDefinitions);
                })
                .peek(ruleGroupDefinition -> {
                    // 黑白名单
                    RuleListDefinition ruleListDefinition = this.buildRuleListDefinition(ruleGroupDefinition);
                    ruleGroupDefinition.setRuleListDefinition(ruleListDefinition);
                })
                .forEach(ruleGroupDefinition -> {
                    // 规则集合
                    List<RuleDefinition> ruleDefinitions = this.buildRuleDefinitions(ruleGroupDefinition);
                    context.initRuleDefinitions(ruleGroupDefinition, ruleDefinitions);
                });
        this.postProcess();

        this.mapping.clear();
        this.mapping = null;

        RuleGroupDefinitionContext.toggle();
    }

    /**
     * 解析预处理
     *
     * @param source 配置源
     */
    private void preProcess(T source) {
        try {
            this.doPreProcess(source);
        } catch (Exception e) {
            throw new RuntimeException("pre processing failed, source: " + source, e);
        }
    }

    /**
     * 配置源解析为{@link RuleGroupDefinition}
     *
     * @param source 配置源
     * @return {@link RuleGroupDefinition}
     */
    private RuleGroupDefinition buildRuleGroupDefinition(T source) {
        try {
            Map<String, Object> map = this.doBuildRuleGroupDefinition(source);
            String id = this.getValueWithMapOrThrow(map, "id");
            String name = this.getValueWithMapOrThrow(map, "name");
            String type = this.getValueWithMapOrThrow(map, "type");
            boolean enabled = this.getValueWithMap(map, "enabled", true);

            RuleGroupDefinition ruleGroupDefinition = new RuleGroupDefinition();
            ruleGroupDefinition.setId(id);
            ruleGroupDefinition.setName(name);
            ruleGroupDefinition.setType(RuleGroupDefinition.Type.valueOf(type));
            ruleGroupDefinition.setEnabled(enabled);
            return ruleGroupDefinition;
        } catch (Exception e) {
            throw new RuntimeException("build RuleGroupDefinition failed, source: " + source, e);
        }
    }

    /**
     * {@link RuleGroupDefinition}解析为{@link RuleDataSourceDefinition}Map
     *
     * @param ruleGroupDefinition 规则组
     * @return {@link RuleDataSourceDefinition}Map
     */
    private Map<String, RuleDataSourceDefinition> buildRuleDataSourceDefinitionMap(RuleGroupDefinition ruleGroupDefinition) {
        try {
            List<Map<String, Object>> ruleDataSources = this.doBuildRuleDataSourceDefinitions(ruleGroupDefinition);
            return ruleDataSources.stream()
                    .map(this::buildRuleDataSourceDefinition)
                    .filter(RuleDataSourceDefinition::isEnabled)
                    .collect(Collectors.toMap(RuleDataSourceDefinition::getId, Function.identity(), (e1, e2) -> {
                        throw new IllegalArgumentException("外部数据源id重复：" + e1 + ", " + e2);
                    }));
        } catch (Exception e) {
            throw new RuntimeException("build RuleDataSourceDefinitionMap failed, " + ruleGroupDefinition, e);
        }
    }

    /**
     * 解析为{@link RuleDataSourceDefinition}
     *
     * @param map 配置源的映射
     * @return {@link RuleDataSourceDefinition}
     */
    private RuleDataSourceDefinition buildRuleDataSourceDefinition(Map<String, Object> map) {
        try {
            String id = this.getValueWithMapOrThrow(map, "id");
            String name = this.getValueWithMapOrThrow(map, "name");
            boolean enabled = this.getValueWithMap(map, "enabled", true);
            String type = this.getValueWithMapOrThrow(map, "type");
            Map<String, String> config = this.getValueWithMapOrThrow(map, "config");
            String scope = this.getValueWithMap(map, "scope", RuleDataSourceDefinition.Scope.request.name());

            RuleDataSourceDefinition definition = new RuleDataSourceDefinition();
            definition.setId(id);
            definition.setType(RuleDataSourceDefinition.Type.valueOf(type));
            definition.setScope(RuleDataSourceDefinition.Scope.valueOf(scope));
            definition.setEnabled(enabled);
            definition.setName(name);
            definition.setConfig(config);
            return definition;
        } catch (Exception e) {
            throw new RuntimeException("build RuleDataSourceDefinition failed, id: " + map.get("id"), e);
        }
    }

    /**
     * {@link RuleGroupDefinition}解析为{@link RuleParamDefinition}集合
     *
     * @param ruleGroupDefinition 规则组
     * @return {@link RuleParamDefinition}集合
     */
    private List<RuleParamDefinition> buildRuleParamDefinitions(RuleGroupDefinition ruleGroupDefinition) {
        try {
            List<Map<String, Object>> list = this.doBuildRuleParamDefinitions(ruleGroupDefinition);
            return list.stream()
                    .map(map -> {
                        String id = this.getValueWithMapOrThrow(map, "id");
                        String name = this.getValueWithMapOrThrow(map, "name");
                        boolean enabled = this.getValueWithMap(map, "enabled", true);
                        String path = this.getValueWithMap(map, "path");
                        String expression = path == null ? this.getValueWithMapOrThrow(map, "expression") : FileUtil.getFileContent(path);

                        RuleParamDefinition definition = new RuleParamDefinition();
                        definition.setId(id);
                        definition.setName(name);
                        definition.setEnabled(enabled);
                        definition.setExpression(expression);
                        return definition;
                    })
                    .filter(RuleParamDefinition::isEnabled)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("build RuleParamDefinitions failed, " + ruleGroupDefinition, e);
        }
    }

    /**
     * 规则黑白名单
     *
     * @param ruleGroupDefinition 规则组
     * @return 规则黑白名单
     */
    private RuleListDefinition buildRuleListDefinition(RuleGroupDefinition ruleGroupDefinition) {
        try {
            Map<String, Object> map = this.doBuildRuleListDefinition(ruleGroupDefinition);
            if (map.isEmpty()) {
                return null;
            }
            String id = this.getValueWithMap(map, "id");
            String name = this.getValueWithMapOrThrow(map, "name");
            String type = this.getValueWithMapOrThrow(map, "type");
            boolean enabled = this.getValueWithMap(map, "enabled", true);

            RuleUnitDefinition ruleUnitDefinition = this.buildRuleUnitDefinition(map);

            RuleListDefinition ruleListDefinition = new RuleListDefinition();
            ruleListDefinition.setId(id);
            ruleListDefinition.setEnabled(enabled);
            ruleListDefinition.setName(name);
            ruleListDefinition.setType(RuleListDefinition.Type.valueOf(type));
            ruleListDefinition.setRuleUnitDefinition(ruleUnitDefinition);
            return ruleListDefinition;
        } catch (Exception e) {
            throw new RuntimeException("build RuleListDefinition failed, " + ruleGroupDefinition, e);
        }
    }

    /**
     * {@link RuleGroupDefinition}解析为{@link RuleDefinition}集合
     *
     * @param ruleGroupDefinition 规则组
     * @return {@link RuleDefinition}集合
     */
    private List<RuleDefinition> buildRuleDefinitions(RuleGroupDefinition ruleGroupDefinition) {
        try {
            List<Map<String, Object>> rules = this.doBuildRuleDefinitions(ruleGroupDefinition);
            if (rules.isEmpty()) {
                throw new IllegalArgumentException("must contain at least one rule");
            }

            List<RuleDefinition> result = rules.stream()
                    .map(this::buildRuleDefinition)
                    .filter(RuleDefinition::isEnabled)
                    .collect(Collectors.toList());

            result.stream().collect(Collectors.toMap(Function.identity(), Function.identity(), (e1, e2) -> {
                throw new IllegalArgumentException("规则id重复：" + e1 + ", " + e2);
            }));

            return result;
        } catch (Exception e) {
            throw new RuntimeException("build RuleDefinitions failed, " + ruleGroupDefinition, e);
        }
    }

    /**
     * 解析为{@link RuleDefinition}规则
     *
     * @param map 配置源的映射
     * @return {@link RuleDefinition}
     */
    private RuleDefinition buildRuleDefinition(Map<String, Object> map) {
        try {
            String id = this.getValueWithMapOrThrow(map, "id");
            String desc = this.getValueWithMapOrThrow(map, "desc");
            String name = this.getValueWithMapOrThrow(map, "name");
            int priority = this.getValueWithMap(map, "priority", 0);
            int score = this.getValueWithMap(map, "score", 0);
            boolean enabled = this.getValueWithMap(map, "enabled", true);
            Map<String, Object> extended = this.buildRuleExtendedProperties(map);

            RuleUnitDefinition ruleUnitDefinition = this.buildRuleUnitDefinition(map);

            RuleDefinition ruleDefinition = new RuleDefinition();
            ruleDefinition.setId(id);
            ruleDefinition.setEnabled(enabled);
            ruleDefinition.setDesc(desc);
            ruleDefinition.setName(name);
            ruleDefinition.setPriority(priority);
            ruleDefinition.setScore(score);
            ruleDefinition.setExtended(extended);
            ruleDefinition.setRuleUnitDefinition(ruleUnitDefinition);
            return ruleDefinition;
        } catch (Exception e) {
            throw new RuntimeException("build RuleDefinition failed, id: " + map.get("id"), e);
        }
    }

    /**
     * 解析为{@link RuleUnitDefinition}规则单元
     *
     * @param map {@link RuleDefinition}/{@link RuleListDefinition}映射
     * @return {@link RuleUnitDefinition}
     */
    private RuleUnitDefinition buildRuleUnitDefinition(Map<String, Object> map) {
        List<Map<String, Object>> units = this.doBuildRuleUnitDefinitions(map);
        if (units.isEmpty()) {
            throw new IllegalArgumentException("must contain at least one unit");
        }

        List<Map<String, Object>> list = new ArrayList<>(units);
        Collections.reverse(list);

        AtomicInteger counter = new AtomicInteger(list.size());
        AtomicReference<RuleUnitDefinition> reference = new AtomicReference<>();
        list.stream()
                .map(this::doBuildRuleUnitDefinition)
                .peek(ruleUnitDefinition -> ruleUnitDefinition.setIndex(counter.getAndDecrement()))
                .filter(RuleUnitDefinition::isEnabled)
                .peek(ruleUnitDefinition -> AviatorEvaluator.getInstance().validate(ruleUnitDefinition.getExpression()))
                .peek(ruleUnitDefinition -> ruleUnitDefinition.setNext(reference.get()))
                .forEach(reference::set);
        return reference.get();
    }

    /**
     * 解析为{@link RuleUnitDefinition}规则单元
     *
     * @param map 配置源的映射
     * @return {@link RuleUnitDefinition}
     */
    private RuleUnitDefinition doBuildRuleUnitDefinition(Map<String, Object> map) {
        boolean enabled = this.getValueWithMap(map, "enabled", true);
        String path = this.getValueWithMap(map, "path");
        String expression = path == null ? this.getValueWithMapOrThrow(map, "expression") : FileUtil.getFileContent(path);
        String dataSource = this.getValueWithMap(map, "dataSource");
        RuleDataSourceDefinition dataSrouceRef = null;
        if (dataSource != null) {
            dataSrouceRef = this.mapping.get(dataSource);
            if (dataSrouceRef == null) {
                throw new IllegalArgumentException("DataSource '" + dataSource + "' not found");
            }
        } else {
            dataSrouceRef = RuleDataSourceDefinition.DEFAULT_INSTANCE;
        }

        RuleUnitDefinition ruleUnitDefinition = new RuleUnitDefinition();
        ruleUnitDefinition.setEnabled(enabled);
        ruleUnitDefinition.setDataSourceRef(dataSrouceRef);
        ruleUnitDefinition.setExpression(expression);
        return ruleUnitDefinition;
    }

    protected <S> S getValueWithMap(Map<String, Object> map, String key) {
        return (S) map.get(key);
    }

    protected <S> S getValueWithMap(Map<String, Object> map, String key, S defaultValue) {
        return (S) map.getOrDefault(key, defaultValue);
    }

    protected <S> S getValueWithMapOrThrow(Map<String, Object> map, String key) {
        if (!map.containsKey(key)) {
            throw new IllegalArgumentException("No such key: " + key);
        }
        return (S) map.get(key);
    }

    /**
     * 获取配置源集合（Xml、Yaml、数据库）
     *
     * @return 配置源集合
     */
    protected abstract List<T> getSources();

    /**
     * 解析预处理
     *
     * @param source 配置源
     */
    protected abstract void doPreProcess(T source);

    /**
     * 解析为{@link RuleGroupDefinition}Map对象
     *
     * @param source 配置源
     * @return {@link RuleGroupDefinition}Map对象
     */
    protected abstract Map<String, Object> doBuildRuleGroupDefinition(T source);

    /**
     * 解析为{@link RuleDataSourceDefinition}映射集合
     *
     * @param ruleGroupDefinition 规则组
     * @return {@link RuleDataSourceDefinition}映射集合
     */
    protected abstract List<Map<String, Object>> doBuildRuleDataSourceDefinitions(RuleGroupDefinition ruleGroupDefinition);

    /**
     * 解析为{@link RuleParamDefinition}映射集合
     *
     * @param ruleGroupDefinition 规则组
     * @return {@link RuleParamDefinition}映射集合
     */
    protected abstract List<Map<String, Object>> doBuildRuleParamDefinitions(RuleGroupDefinition ruleGroupDefinition);

    /**
     * 黑白名单映射
     *
     * @param ruleGroupDefinition 规则组
     * @return 黑白名单
     */
    protected abstract Map<String, Object> doBuildRuleListDefinition(RuleGroupDefinition ruleGroupDefinition);

    /**
     * 解析为{@link RuleDefinition}映射集合
     *
     * @param ruleGroupDefinition 规则组
     * @return {@link RuleDefinition}映射集合
     */
    protected abstract List<Map<String, Object>> doBuildRuleDefinitions(RuleGroupDefinition ruleGroupDefinition);

    /**
     * 构建规则扩展属性
     *
     * @param map {@link RuleDefinition}映射
     * @return 扩展属性
     */
    protected abstract Map<String, Object> buildRuleExtendedProperties(Map<String, Object> map);

    /**
     * 解析为{@link RuleUnitDefinition}映射集合
     *
     * @param map {@link RuleDefinition}/{@link RuleListDefinition}映射
     * @return {@link RuleUnitDefinition}映射集合
     */
    protected abstract List<Map<String, Object>> doBuildRuleUnitDefinitions(Map<String, Object> map);

    /**
     * 解析后处理
     */
    protected abstract void postProcess();

}
