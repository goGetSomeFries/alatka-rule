package com.alatka.rule;

import com.alatka.rule.core.RuleEngine;
import com.alatka.rule.core.datasource.DatabaseExternalDataSource;
import com.alatka.rule.core.datasource.ExternalDataSource;
import com.alatka.rule.core.datasource.ExternalDataSourceFactory;
import com.alatka.rule.core.datasource.RedisExternalDataSource;
import com.alatka.rule.core.definition.DatabaseRuleDefinitionBuilder;
import com.alatka.rule.core.definition.RuleDefinitionBuilder;
import com.alatka.rule.core.definition.XmlRuleDefinitionBuilder;
import com.alatka.rule.core.definition.YamlRuleDefinitionBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(AlatkaRuleProperties.class)
@ConditionalOnProperty(value = "alatka.rule.enabled", havingValue = "true", matchIfMissing = true)
public class AlatkaRuleAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = "alatka.rule.type", havingValue = "yaml", matchIfMissing = true)
    public RuleDefinitionBuilder yamlRuleDefinitionBuilder(AlatkaRuleProperties properties) {
        String classpath = properties.getClasspath();
        RuleDefinitionBuilder builder = new YamlRuleDefinitionBuilder(classpath == null ? "" : classpath);
        builder.build();
        return builder;
    }

    @Bean
    @ConditionalOnProperty(value = "alatka.rule.type", havingValue = "xml")
    public RuleDefinitionBuilder xmlRuleDefinitionBuilder(AlatkaRuleProperties properties) {
        String classpath = properties.getClasspath();
        RuleDefinitionBuilder builder = new XmlRuleDefinitionBuilder(classpath == null ? "" : classpath);
        builder.build();
        return builder;
    }

    @Bean
    @ConditionalOnProperty(value = "alatka.rule.type", havingValue = "database")
    public RuleDefinitionBuilder databaseRuleDefinitionBuilder(DataSource dataSource) {
        RuleDefinitionBuilder builder = new DatabaseRuleDefinitionBuilder(dataSource);
        builder.build();
        return builder;
    }

    @Bean
    public RuleEngine ruleEngine() {
        return new RuleEngine();
    }

    @Bean
    public ExternalDataSourceFactory externalDataSourceFactory(ApplicationContext context) {
        ExternalDataSourceFactory factory = ExternalDataSourceFactory.getInstance();
        Map<String, ExternalDataSource> map = context.getBeansOfType(ExternalDataSource.class);
        map.values().stream().forEach(factory::init);
        return factory;
    }

    @Bean
//    @ConditionalOnBean(name = "dataSource")
    @ConditionalOnMissingBean
    public ExternalDataSource databaseExternalDataSource(DataSource dataSource) {
        // TODO
        return new DatabaseExternalDataSource(dataSource);
    }

    @Configuration
    @ConditionalOnClass(NamedParameterJdbcTemplate.class)
    public static class DatabaseExternalDataSourceAutoConfiguration {

        @Bean
        public ExternalDataSource databaseExternalDataSource(NamedParameterJdbcTemplate jdbcTemplate) {
            return new DatabaseExternalDataSource(jdbcTemplate);
        }

    }

    @Configuration
    @ConditionalOnClass(RedisTemplate.class)
    public static class RedisExternalDataSourceAutoConfiguration {

        @Bean
        public ExternalDataSource redisExternalDataSource(RedisTemplate<Object, Object> redisTemplate) {
            return new RedisExternalDataSource(redisTemplate);
        }

    }

}
