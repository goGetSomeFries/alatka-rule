package com.alatka.rule.admin.service;


import com.alatka.rule.admin.entity.RuleGroupDefinition;
import com.alatka.rule.admin.repository.RuleGroupDefinitionRepository;
import org.springframework.stereotype.Service;

@Service
public class RuleGroupDefinitionService {

    private RuleGroupDefinitionRepository ruleGroupDefinitionRepository;

    public void save(RuleGroupDefinition ruleGroupDefinition) {
        ruleGroupDefinitionRepository.save(ruleGroupDefinition);
    }

    public void setRuleGroupDefinitionRepository(RuleGroupDefinitionRepository ruleGroupDefinitionRepository) {
        this.ruleGroupDefinitionRepository = ruleGroupDefinitionRepository;
    }
}
