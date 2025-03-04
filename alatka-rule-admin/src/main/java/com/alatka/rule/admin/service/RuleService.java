package com.alatka.rule.admin.service;


import com.alatka.rule.admin.entity.RuleDefinition;
import com.alatka.rule.admin.model.rule.RuleReq;
import com.alatka.rule.admin.repository.RuleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RuleService {

    private RuleRepository ruleRepository;

    private RuleExtendedService ruleExtendedService;

    public Long save(RuleReq req) {
        RuleDefinition entity = new RuleDefinition();
        BeanUtils.copyProperties(req, entity);

        Long ruleId = ruleRepository.save(entity).getId();
        ruleExtendedService.save(req.getExtended(), ruleId, entity.getGroupKey());
        return ruleId;
    }


    @Autowired
    public void setRuleRepository(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    @Autowired
    public void setRuleExtendedService(RuleExtendedService ruleExtendedService) {
        this.ruleExtendedService = ruleExtendedService;
    }
}
