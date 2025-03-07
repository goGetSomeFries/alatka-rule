package com.alatka.rule.admin.repository;

import com.alatka.rule.admin.entity.RuleDatasourceDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleDatasourceRepository extends JpaRepository<RuleDatasourceDefinition, Long>, JpaSpecificationExecutor<RuleDatasourceDefinition> {
}
