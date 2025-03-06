package com.alatka.rule.admin.service;


import com.alatka.rule.admin.entity.RuleExtDefinition;
import com.alatka.rule.admin.repository.RuleExtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class RuleExtService {

    private RuleExtRepository ruleExtRepository;

    public void save(Map<String, Object> extended, Long ruleId, String groupKey) {
        List<RuleExtDefinition> extendedList = extended.entrySet().stream().map(entry -> {
            RuleExtDefinition entity = new RuleExtDefinition();
            entity.setRuleId(ruleId);
            entity.setGroupKey(groupKey);
            entity.setKey(entry.getKey());
            entity.setValue(entry.getValue() == null ? null : entry.getValue().toString());
            return entity;
        }).collect(Collectors.toList());
        ruleExtRepository.saveAll(extendedList);
    }

    public void deleteByRuleId(Long ruleId) {
        List<RuleExtDefinition> list = this.queryByRuleId(ruleId);
        ruleExtRepository.deleteAllInBatch(list);
    }

    public List<RuleExtDefinition> queryByRuleId(Long ruleId) {
        RuleExtDefinition entity = new RuleExtDefinition();
        entity.setRuleId(ruleId);
        return ruleExtRepository.findAll(Example.of(entity));
    }

    @Autowired
    public void setRuleExtRepository(RuleExtRepository ruleExtRepository) {
        this.ruleExtRepository = ruleExtRepository;
    }
}
