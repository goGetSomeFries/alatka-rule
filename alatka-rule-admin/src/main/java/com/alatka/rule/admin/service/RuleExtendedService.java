package com.alatka.rule.admin.service;


import com.alatka.rule.admin.entity.RuleExtendedDefinition;
import com.alatka.rule.admin.repository.RuleExtendedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class RuleExtendedService {

    private RuleExtendedRepository ruleExtendedRepository;

    public void save(Map<String, Object> extended, Long ruleId, String groupKey) {
        List<RuleExtendedDefinition> extendedList = extended.entrySet().stream().map(entry -> {
            RuleExtendedDefinition entity = new RuleExtendedDefinition();
            entity.setRuleId(ruleId);
            entity.setGroupKey(groupKey);
            entity.setKey(entry.getKey());
            entity.setValue(entry.getValue() == null ? null : entry.getValue().toString());
            return entity;
        }).collect(Collectors.toList());
        ruleExtendedRepository.saveAll(extendedList);
    }

    public void deleteByRuleId(Long ruleId) {
        List<RuleExtendedDefinition> list = this.queryByRuleId(ruleId);
        List<Long> ids = list.stream().map(RuleExtendedDefinition::getId).collect(Collectors.toList());
        ruleExtendedRepository.deleteAllById(ids);
    }

    public List<RuleExtendedDefinition> queryByRuleId(Long ruleId) {
        RuleExtendedDefinition entity = new RuleExtendedDefinition();
        entity.setRuleId(ruleId);
        return ruleExtendedRepository.findAll(Example.of(entity));
    }

    @Autowired
    public void setRuleExtendedRepository(RuleExtendedRepository ruleExtendedRepository) {
        this.ruleExtendedRepository = ruleExtendedRepository;
    }
}
