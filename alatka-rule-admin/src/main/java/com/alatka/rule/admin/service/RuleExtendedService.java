package com.alatka.rule.admin.service;


import com.alatka.rule.admin.entity.RuleDatasourceDefinition;
import com.alatka.rule.admin.entity.RuleExtendedDefinition;
import com.alatka.rule.admin.model.ruledatasource.RuleDatasourcePageReq;
import com.alatka.rule.admin.model.ruledatasource.RuleDatasourceReq;
import com.alatka.rule.admin.model.ruledatasource.RuleDatasourceRes;
import com.alatka.rule.admin.repository.RuleExtendedRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
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
//            entity.setValue(entry.getValue());
            return entity;
        }).collect(Collectors.toList());
        ruleExtendedRepository.saveAll(extendedList);
    }

    @Autowired
    public void setRuleExtendedRepository(RuleExtendedRepository ruleExtendedRepository) {
        this.ruleExtendedRepository = ruleExtendedRepository;
    }
}
