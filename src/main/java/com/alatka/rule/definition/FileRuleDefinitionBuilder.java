package com.alatka.rule.definition;

import com.alatka.rule.context.RuleGroupDefinition;
import com.alatka.rule.util.FileUtil;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 文件规则构建器
 *
 * @author whocares
 * @see XmlRuleDefinitionBuilder
 * @see YamlRuleDefinitionBuilder
 */
public abstract class FileRuleDefinitionBuilder extends AbstractRuleDefinitionBuilder<Path> {

    private static final String SUFFIX = ".rule";

    private String classpath;

    protected Map<String, Object> rootModel;

    public FileRuleDefinitionBuilder() {
        this("");
    }

    public FileRuleDefinitionBuilder(String classpath) {
        this.classpath = classpath;
    }

    @Override
    protected List<Path> getSources() {
        return Arrays.stream(suffix())
                .flatMap(suffix -> FileUtil.getClasspathFiles(this.classpath, "*" + SUFFIX + suffix).stream())
                .collect(Collectors.toList());
    }

    @Override
    protected void preProcess(Path source) {
        this.rootModel = this.initRootModel(source);
    }

    @Override
    protected Map<String, Object> doBuildRuleGroupDefinition(Path source) {
        String fileName = source.toFile().getName();

        String id = fileName.substring(0, fileName.lastIndexOf(SUFFIX));
        String name = this.getValueWithMap(this.rootModel, "name");
        String type = this.getValueWithMap(this.rootModel, "type", RuleGroupDefinition.Type.all.name());
        boolean enabled = this.getValueWithMap(this.rootModel, "enabled", true);

        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("enabled", enabled);
        result.put("type", type);
        return result;
    }

    @Override
    protected void postProcess() {
        // 释放对象
        this.rootModel = null;
    }

    /**
     * 初始化配置
     *
     * @param source 配置源
     * @return 配置Map对象
     */
    protected abstract Map<String, Object> initRootModel(Path source);

    /**
     * 文件后缀
     *
     * @return 文件后缀集合
     */
    protected abstract String[] suffix();

}
