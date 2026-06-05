package com.alatka.rule.admin.support;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class DefaultAuditorAware implements AuditorAware<String> {

    public static final String BEAN_NAME = "defaultAuditorAware";

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("anonymous");
    }
}
