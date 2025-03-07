package com.alatka.rule.admin.service;


import com.alatka.rule.admin.entity.RuleParamDefinition;
import com.alatka.rule.admin.model.ruleparam.RuleParamPageReq;
import com.alatka.rule.admin.model.ruleparam.RuleParamReq;
import com.alatka.rule.admin.model.ruleparam.RuleParamRes;
import com.alatka.rule.admin.repository.RuleParamRepository;
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
public class RuleParamService {

    private RuleParamRepository ruleParamRepository;

    public Long create(RuleParamReq req) {
        RuleParamDefinition entity = new RuleParamDefinition();
        BeanUtils.copyProperties(req, entity);

        RuleParamDefinition condition = new RuleParamDefinition();
        condition.setKey(entity.getKey());
        condition.setGroupKey(entity.getGroupKey());
        boolean exists = ruleParamRepository.exists(this.condition(condition));
        if (exists) {
            throw new IllegalArgumentException("key : <" + condition.getKey() + "> is present already");
        }

        return ruleParamRepository.save(entity).getId();
    }

    public void update(RuleParamReq req) {
        RuleParamDefinition entity = new RuleParamDefinition();
        BeanUtils.copyProperties(req, entity);

        boolean exists = ruleParamRepository.existsById(entity.getId());
        if (!exists) {
            throw new IllegalArgumentException("id : <" + entity.getId() + "> not found");
        }
        ruleParamRepository.save(entity);
    }

    public void delete(Long id) {
        RuleParamDefinition entity = ruleParamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id: <" + id + "> not found"));
        ruleParamRepository.delete(entity);
    }

    public Page<RuleParamRes> queryPage(RuleParamPageReq pageReq) {
        RuleParamDefinition condition = new RuleParamDefinition();
        BeanUtils.copyProperties(pageReq, condition);

        return ruleParamRepository.findAll(this.condition(condition), pageReq.build())
                .map(entity -> {
                    RuleParamRes res = new RuleParamRes();
                    BeanUtils.copyProperties(entity, res);
                    return res;
                });
    }

    private Specification<RuleParamDefinition> condition(RuleParamDefinition condition) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if (condition.getId() != null) {
                list.add(criteriaBuilder.equal(root.get("id").as(Long.class), condition.getId()));
            }
            if (condition.getKey() != null) {
                list.add(criteriaBuilder.equal(root.get("key").as(String.class), condition.getKey()));
            }
            if (condition.getName() != null) {
                list.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + condition.getName() + "%"));
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
    public void setRuleParamRepository(RuleParamRepository ruleParamRepository) {
        this.ruleParamRepository = ruleParamRepository;
    }
}
