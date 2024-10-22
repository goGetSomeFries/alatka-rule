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
    private static final String KEY_SET_KEY = "setKey";

    private RedisTemplate<String, Object> redisTemplate;

    public RedisExternalDataSource(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected Object doBuildContext(RuleDataSourceDefinition definition, Map<String, Object> paramContext) {
        Map<String, Object> config = definition.getConfig();
        Type type = Type.valueOf(config.get(KEY_TYPE).toString());
        String key = config.get(KEY_KEY).toString();
        switch (type) {
            case list:
                return this.redisTemplate.opsForList().range(key, 0, -1);
            case string:
                return this.redisTemplate.opsForValue().get(key);
            case hash:
                Object hashKey = paramContext.get(config.get(KEY_HASH_KEY));
                return this.redisTemplate.opsForHash().get(key, hashKey);
            case set:
                // 用于黑白名单判断
                Object setValue = paramContext.get(config.get(KEY_SET_KEY));
                return this.redisTemplate.opsForSet().isMember(key, setValue);
            default:
                throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }

    @Override
    public RuleDataSourceDefinition.Type type() {
        return RuleDataSourceDefinition.Type.redis;
    }

    private static enum Type {
        string, hash, set, list
    }
}
