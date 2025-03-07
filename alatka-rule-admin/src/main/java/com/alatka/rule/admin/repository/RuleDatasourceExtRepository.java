package com.alatka.rule.admin.repository;

import com.alatka.rule.admin.entity.RuleDatasourceExtDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleDatasourceExtRepository extends JpaRepository<RuleDatasourceExtDefinition, Long>, JpaSpecificationExecutor<RuleDatasourceExtDefinition> {
}
