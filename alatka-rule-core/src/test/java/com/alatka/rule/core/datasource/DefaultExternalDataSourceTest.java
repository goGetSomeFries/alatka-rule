package com.alatka.rule.core.datasource;

import com.alatka.rule.core.context.RuleDataSourceDefinition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DefaultExternalDataSourceTest {

    @Test
    @DisplayName("type()")
    void test01() {
        ExternalDataSource externalDataSource = new DefaultExternalDataSource();
        Assertions.assertSame(RuleDataSourceDefinition.Type.current, externalDataSource.type());
    }

    @Test
    @DisplayName("doBuildContext()")
    void test02() {
        DefaultExternalDataSource externalDataSource = new DefaultExternalDataSource();
        Object value = externalDataSource.doBuildContext(null, null);

        Assertions.assertNull(value);
    }
}
