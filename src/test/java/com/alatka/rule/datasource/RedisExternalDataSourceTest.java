package com.alatka.rule.datasource;

import com.alatka.rule.context.RuleDataSourceDefinition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RedisExternalDataSourceTest {

    @Test
    @DisplayName("type()")
    void test01() {
        ExternalDataSource externalDataSource = new RedisExternalDataSource(null);
        Assertions.assertSame(RuleDataSourceDefinition.Type.redis, externalDataSource.type());
    }

    @Test
    @DisplayName("doBuildContext()")
    void test02() {
        RedisTemplate<String, Object> redisTemplate = Mockito.mock(RedisTemplate.class);
        ListOperations<String, Object> listOperations = Mockito.mock(ListOperations.class);
        ValueOperations<String, Object> valueOperations = Mockito.mock(ValueOperations.class);
        Mockito.when(redisTemplate.opsForList()).thenReturn(listOperations);
        Mockito.when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        Mockito.when(listOperations.range(Mockito.any(), Mockito.anyLong(), Mockito.anyLong())).thenReturn(Collections.emptyList());
        Mockito.when(valueOperations.get(Mockito.anyString())).thenReturn(valueOperations);
        RedisExternalDataSource externalDataSource = new RedisExternalDataSource(redisTemplate);

        Map<String, String> config = new HashMap<>();
        Assertions.assertThrows(IllegalArgumentException.class, () -> externalDataSource.doBuildContext(config, null), "type is required");

        config.put("type", "");
        Assertions.assertThrows(IllegalArgumentException.class, () -> externalDataSource.doBuildContext(config, null), "type is required");

        config.put("type", "list");
        Assertions.assertThrows(IllegalArgumentException.class, () -> externalDataSource.doBuildContext(config, null), "key is required");

        config.put("key", "");
        Assertions.assertThrows(IllegalArgumentException.class, () -> externalDataSource.doBuildContext(config, null), "key is required");

    }
}
