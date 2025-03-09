package com.alatka.rule.admin.repository;

import com.alatka.rule.admin.entity.RuleExtDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleExtRepository extends JpaRepository<RuleExtDefinition, Long>, JpaSpecificationExecutor<RuleExtDefinition> {
}
