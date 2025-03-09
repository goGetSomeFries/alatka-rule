package com.alatka.rule.admin.repository;

import com.alatka.rule.admin.entity.RuleParamDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleParamRepository extends JpaRepository<RuleParamDefinition, Long>, JpaSpecificationExecutor<RuleParamDefinition> {
}
