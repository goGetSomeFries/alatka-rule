package com.alatka.rule.datasource;

import com.alatka.rule.context.RuleDataSourceDefinition;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;

/**
 * redis外部数据源
 *
 * @author whocares
 */
public class RedisExternalDataSource extends AbstractExternalDataSource {

    private static final String KEY_TYPE = "type";
    private static final String KEY_KEY = "key";
    private static final String KEY_HASH_KEY = "hashKey";

    private RedisTemplate<String, String> redisTemplate;

    public RedisExternalDataSource(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected Object doBuildContext(RuleDataSourceDefinition definition, Map<String, Object> paramContext) {
        Map<String, Object> config = definition.getConfig();
        Type type = Type.valueOf(config.get(KEY_TYPE).toString());
        String key = config.get(KEY_KEY).toString();
        switch (type) {
            case list:
            case string:
            case hash:
            case set:
            default:
        }
        return null;
    }

    @Override
    public RuleDataSourceDefinition.Type type() {
        return RuleDataSourceDefinition.Type.redis;
    }

    private static enum Type {
        string, hash, set, list
    }
}
