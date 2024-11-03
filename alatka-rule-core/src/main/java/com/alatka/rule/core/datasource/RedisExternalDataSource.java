package com.alatka.rule.core.datasource;

import com.alatka.rule.core.context.RuleDataSourceDefinition;
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
    private static final String KEY_SET_VALUE = "setValue";

    private RedisTemplate<Object, Object> redisTemplate;

    public RedisExternalDataSource(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected Object doBuildContext(Map<String, String> config, Map<String, Object> paramContext) {
        String type = this.getWithConfig(config, KEY_TYPE);
        String key = this.getWithConfig(config, KEY_KEY);
        switch (Type.valueOf(type)) {
            case list:
                return this.redisTemplate.opsForList().range(key, 0, -1);
            case string:
                return this.redisTemplate.opsForValue().get(key);
            case hash:
                Object hashKey = paramContext.get(this.getWithConfig(config, KEY_HASH_KEY));
                if (hashKey == null) {
                    return null;
                }
                return this.redisTemplate.opsForHash().get(key, hashKey.toString());
            case set:
                // 用于黑白名单判断
                Object setValue = paramContext.get(this.getWithConfig(config, KEY_SET_VALUE));
                if (setValue == null) {
                    return null;
                }
                return this.redisTemplate.opsForSet().isMember(key, setValue.toString());
            default:
                throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }

    @Override
    public RuleDataSourceDefinition.Type type() {
        return RuleDataSourceDefinition.Type.redis;
    }

    private enum Type {
        string, hash, set, list
    }
}
