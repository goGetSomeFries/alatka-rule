package com.alatka.rule.admin.repository;

import com.alatka.rule.admin.entity.RuleGroupDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleGroupRepository extends JpaRepository<RuleGroupDefinition, Long>, JpaSpecificationExecutor<RuleGroupDefinition> {
}
