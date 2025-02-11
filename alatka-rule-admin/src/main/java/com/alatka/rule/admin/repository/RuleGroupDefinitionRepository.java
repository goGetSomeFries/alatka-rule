package com.alatka.rule.admin.repository;

import com.alatka.rule.admin.entity.RuleGroupDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleGroupDefinitionRepository extends JpaRepository<RuleGroupDefinition, Long> {
}
