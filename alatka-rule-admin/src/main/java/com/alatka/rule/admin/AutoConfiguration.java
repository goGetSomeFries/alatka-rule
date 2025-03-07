package com.alatka.rule.admin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ThreadPoolExecutor;

@EnableJpaRepositories
@EntityScan
@ComponentScan
@Configuration
public class AutoConfiguration {

    @Value("${alatka.rule.admin.rest.connectionTimeout:5000}")
    private int connectionTimeout;

    @Value("${alatka.rule.admin.rest.readTimeout:5000}")
    private int readTimeout;

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectionTimeout);
        factory.setReadTimeout(readTimeout);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(factory);
        return restTemplate;
    }

    @Value("${alatka.rule.admin.threadPool.corePoolSize:5}")
    private int corePoolSize;

    @Value("${alatka.rule.admin.threadPool.maxPoolSize:10}")
    private int maxPoolSize;

    @Value("${alatka.rule.admin.threadPool.queueCapacity:1}")
    private int queueCapacity;

    @Value("${alatka.rule.admin.threadPool.keepAliveSeconds:600}")
    private int keepAliveSeconds;

    @Value("${alatka.rule.admin.threadPool.threadNamePrefix:alatka-rule-admin-thread-}")
    private String threadNamePrefix;

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(corePoolSize);
        taskExecutor.setMaxPoolSize(maxPoolSize);
        taskExecutor.setQueueCapacity(queueCapacity);
        taskExecutor.setKeepAliveSeconds(keepAliveSeconds);
        taskExecutor.setThreadNamePrefix(threadNamePrefix);
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return taskExecutor;
    }
}
