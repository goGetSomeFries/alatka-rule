package com.alatka.rule.admin.repository;

import com.alatka.rule.admin.entity.RuleVariableDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleVariableRepository extends JpaRepository<RuleVariableDefinition, Long>, JpaSpecificationExecutor<RuleVariableDefinition> {
}
