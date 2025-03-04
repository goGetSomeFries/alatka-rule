package com.alatka.rule.admin.repository;

import com.alatka.rule.admin.entity.RuleExtendedDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleExtendedRepository extends JpaRepository<RuleExtendedDefinition, Long>, JpaSpecificationExecutor<RuleExtendedDefinition> {
}
