package com.alatka.rule.admin.service;


import com.alatka.rule.admin.entity.RuleUnitDefinition;
import com.alatka.rule.admin.model.ruleunit.RuleUnitReq;
import com.alatka.rule.admin.model.ruleunit.RuleUnitRes;
import com.alatka.rule.admin.repository.RuleUnitRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RuleUnitService {

    private RuleUnitRepository ruleUnitRepository;

    public Long create(RuleUnitReq req) {
        RuleUnitDefinition entity = new RuleUnitDefinition();
        BeanUtils.copyProperties(req, entity);

        return ruleUnitRepository.save(entity).getId();
    }

    public void update(RuleUnitReq req) {
        RuleUnitDefinition entity = new RuleUnitDefinition();
        BeanUtils.copyProperties(req, entity);

        boolean exists = ruleUnitRepository.existsById(entity.getId());
        if (!exists) {
            throw new IllegalArgumentException("id : <" + entity.getId() + "> not found");
        }
        ruleUnitRepository.save(entity);
    }

    public void delete(Long id) {
        RuleUnitDefinition entity = ruleUnitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id: <" + id + "> not found"));
        ruleUnitRepository.delete(entity);
    }

    public void deleteByRuleId(Long ruleId) {
        RuleUnitDefinition condition = new RuleUnitDefinition();
        condition.setRuleId(ruleId);
        List<RuleUnitDefinition> list = ruleUnitRepository.findAll(Example.of(condition));
        List<Long> ids = list.stream().map(RuleUnitDefinition::getId).collect(Collectors.toList());
        ruleUnitRepository.deleteAllById(ids);
    }

    public List<RuleUnitRes> queryByRuleId(Long ruleId) {
        RuleUnitDefinition condition = new RuleUnitDefinition();
        condition.setRuleId(ruleId);
        List<RuleUnitDefinition> list = ruleUnitRepository.findAll(Example.of(condition));
        return list.stream().map(entity -> {
            RuleUnitRes res = new RuleUnitRes();
            BeanUtils.copyProperties(entity, res);
            return res;
        }).collect(Collectors.toList());
    }

    @Autowired
    public void setRuleUnitRepository(RuleUnitRepository ruleUnitRepository) {
        this.ruleUnitRepository = ruleUnitRepository;
    }
}
