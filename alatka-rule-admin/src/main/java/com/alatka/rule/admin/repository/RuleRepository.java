package com.alatka.rule.admin.repository;

import com.alatka.rule.admin.entity.RuleDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleRepository extends JpaRepository<RuleDefinition, Long>, JpaSpecificationExecutor<RuleDefinition> {
}
