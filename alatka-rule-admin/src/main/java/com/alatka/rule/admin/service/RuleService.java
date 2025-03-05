package com.alatka.rule.admin.service;


import com.alatka.rule.admin.entity.RuleDefinition;
import com.alatka.rule.admin.model.rule.RulePageReq;
import com.alatka.rule.admin.model.rule.RuleReq;
import com.alatka.rule.admin.model.rule.RuleRes;
import com.alatka.rule.admin.repository.RuleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RuleService {

    private RuleRepository ruleRepository;

    private RuleExtendedService ruleExtendedService;

    private RuleUnitService ruleUnitService;

    public Long create(RuleReq req) {
        RuleDefinition entity = new RuleDefinition();
        BeanUtils.copyProperties(req, entity);

        Long ruleId = ruleRepository.save(entity).getId();
        ruleExtendedService.save(req.getExtended(), ruleId, entity.getGroupKey());
        return ruleId;
    }

    public void update(RuleReq req) {
        boolean exists = ruleRepository.existsById(req.getId());
        if (!exists) {
            throw new IllegalArgumentException("id : <" + req.getId() + "> not found");
        }

        RuleDefinition entity = new RuleDefinition();
        BeanUtils.copyProperties(req, entity);
        ruleRepository.save(entity);
        ruleExtendedService.deleteByRuleId(entity.getId());
        ruleExtendedService.save(req.getExtended(), entity.getId(), entity.getGroupKey());
    }

    public void delete(Long id) {
        RuleDefinition entity = ruleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("rule id: " + id + " not found"));
        ruleRepository.delete(entity);

        ruleExtendedService.deleteByRuleId(id);

        ruleUnitService.deleteByRuleId(id);
    }

    public Page<RuleRes> queryPage(RulePageReq pageReq) {
        RuleDefinition condition = new RuleDefinition();
        BeanUtils.copyProperties(pageReq, condition);

        return ruleRepository.findAll(this.condition(condition), pageReq.build())
                .map(entity -> {
                    RuleRes res = new RuleRes();
                    BeanUtils.copyProperties(entity, res);
                    ruleExtendedService.queryByRuleId(entity.getId())
                            .forEach(extended -> res.setExtended(extended.getKey(), extended.getValue()));
                    return res;
                });
    }

    private Specification<RuleDefinition> condition(RuleDefinition condition) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if (condition.getId() != null) {
                list.add(criteriaBuilder.equal(root.get("id").as(Long.class), condition.getId()));
            }
            if (condition.getName() != null) {
                list.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + condition.getName() + "%"));
            }
            if (condition.getDesc() != null) {
                list.add(criteriaBuilder.like(root.get("desc").as(String.class), "%" + condition.getDesc() + "%"));
            }
            if (condition.getEnabled() != null) {
                list.add(criteriaBuilder.equal(root.get("enabled").as(Boolean.class), condition.getEnabled()));
            }
            if (condition.getGroupKey() != null) {
                list.add(criteriaBuilder.equal(root.get("groupKey").as(String.class), condition.getGroupKey()));
            }

            return criteriaBuilder.and(list.toArray(new Predicate[0]));
        };
    }

    @Autowired
    public void setRuleRepository(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    @Autowired
    public void setRuleExtendedService(RuleExtendedService ruleExtendedService) {
        this.ruleExtendedService = ruleExtendedService;
    }

    @Autowired
    public void setRuleUnitService(RuleUnitService ruleUnitService) {
        this.ruleUnitService = ruleUnitService;
    }
}
