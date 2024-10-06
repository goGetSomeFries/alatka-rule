package com.alatka.rule.definition;

import com.alatka.rule.util.FileUtil;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class FileRuleDefinitionBuilder extends AbstractRuleDefinitionBuilder<Path> {

    private static final String SUFFIX = ".rule";

    private String classpath;

    public FileRuleDefinitionBuilder() {
        this.classpath = "";
    }

    public FileRuleDefinitionBuilder(String classpath) {
        this.classpath = classpath;
    }

    @Override
    protected List<Path> getSources() {
        return Arrays.stream(suffix())
                .flatMap(suffix -> FileUtil.getClasspathFiles(this.classpath, SUFFIX + suffix).stream())
                .collect(Collectors.toList());
    }

    protected abstract String[] suffix();

}
