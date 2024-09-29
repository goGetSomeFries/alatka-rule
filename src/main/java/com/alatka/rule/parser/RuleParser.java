package com.alatka.rule.parser;

import java.util.Map;

public interface RuleParser {

    Map<String, Object> getEnv(Map<String, Object> env, boolean flag);

}
