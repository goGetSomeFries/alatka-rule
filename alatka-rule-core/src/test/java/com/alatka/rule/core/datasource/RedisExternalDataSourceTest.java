package com.alatka.rule.core.datasource;

import com.alatka.rule.core.context.RuleDataSourceDefinition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.core.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
        RedisTemplate<Object, Object> redisTemplate = Mockito.mock(RedisTemplate.class);
        ListOperations<Object, Object> listOperations = Mockito.mock(ListOperations.class);
        ValueOperations<Object, Object> valueOperations = Mockito.mock(ValueOperations.class);
        HashOperations<Object, Object, Object> hashOperations = Mockito.mock(HashOperations.class);
        SetOperations<Object, Object> setOperations = Mockito.mock(SetOperations.class);
        Mockito.when(redisTemplate.opsForList()).thenReturn(listOperations);
        Mockito.when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        Mockito.when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        Mockito.when(redisTemplate.opsForSet()).thenReturn(setOperations);
        Mockito.when(listOperations.range(Mockito.any(), Mockito.anyLong(), Mockito.anyLong())).thenReturn(Collections.emptyList());
        Mockito.when(valueOperations.get(Mockito.any())).thenReturn("1234");
        Mockito.when(hashOperations.get(Mockito.any(), Mockito.any())).thenReturn("AAAAAA");
        Mockito.when(setOperations.isMember(Mockito.any(), Mockito.any())).thenReturn(true);
        RedisExternalDataSource externalDataSource = new RedisExternalDataSource(redisTemplate);

        Map<String, String> config = new HashMap<>();
        Assertions.assertThrows(IllegalArgumentException.class, () -> externalDataSource.doBuildContext(config, null), "type is required");

        config.put("type", "");
        Assertions.assertThrows(IllegalArgumentException.class, () -> externalDataSource.doBuildContext(config, null), "type is required");

        config.put("type", "list");
        Assertions.assertThrows(IllegalArgumentException.class, () -> externalDataSource.doBuildContext(config, null), "key is required");

        config.put("key", "");
        Assertions.assertThrows(IllegalArgumentException.class, () -> externalDataSource.doBuildContext(config, null), "key is required");

        config.put("key", "xxxx");
        Assertions.assertInstanceOf(List.class, externalDataSource.doBuildContext(config, null));

        config.put("type", "string");
        Assertions.assertEquals("1234", externalDataSource.doBuildContext(config, null));

        config.put("type", "hash");
        Assertions.assertThrows(IllegalArgumentException.class, () -> externalDataSource.doBuildContext(config, null), "hashKey is required");

        config.put("hashKey", "");
        Assertions.assertThrows(IllegalArgumentException.class, () -> externalDataSource.doBuildContext(config, null), "hashKey is required");

        config.put("hashKey", "v_card");
        Assertions.assertNull(externalDataSource.doBuildContext(config, Collections.emptyMap()));

        Assertions.assertEquals("AAAAAA", externalDataSource.doBuildContext(config, Collections.singletonMap("v_card", "testing")));

        config.put("type", "set");
        Assertions.assertThrows(IllegalArgumentException.class, () -> externalDataSource.doBuildContext(config, null), "setValue is required");

        config.put("setValue", "");
        Assertions.assertThrows(IllegalArgumentException.class, () -> externalDataSource.doBuildContext(config, null), "setValue is required");

        config.put("setValue", "v_card");
        Assertions.assertNull(externalDataSource.doBuildContext(config, Collections.emptyMap()));

        Assertions.assertTrue((Boolean) externalDataSource.doBuildContext(config, Collections.singletonMap("v_card", "testing")));
    }
}
