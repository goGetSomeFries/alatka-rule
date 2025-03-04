package com.alatka.rule.admin.service;


import com.alatka.rule.admin.entity.RuleGroupDefinition;
import com.alatka.rule.admin.model.rulegroup.RuleGroupPageReq;
import com.alatka.rule.admin.model.rulegroup.RuleGroupReq;
import com.alatka.rule.admin.model.rulegroup.RuleGroupRes;
import com.alatka.rule.admin.repository.RuleGroupRepository;
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
public class RuleGroupService {

    private RuleGroupRepository ruleGroupRepository;

    public Long save(RuleGroupReq req) {
        RuleGroupDefinition entity = new RuleGroupDefinition();
        BeanUtils.copyProperties(req, entity);

        RuleGroupDefinition condition = new RuleGroupDefinition();
        condition.setKey(entity.getKey());
        boolean exists = ruleGroupRepository.exists(this.condition(condition));
        if (exists) {
            throw new IllegalArgumentException("key : <" + condition.getKey() + "> is present already");
        }

        return ruleGroupRepository.save(entity).getId();
    }

    public void update(RuleGroupReq req) {
        RuleGroupDefinition entity = new RuleGroupDefinition();
        BeanUtils.copyProperties(req, entity);

        RuleGroupDefinition theOne = ruleGroupRepository.findById(entity.getId())
                .orElseThrow(() -> new IllegalArgumentException("id : <" + entity.getId() + "> not found"));
        if (!theOne.getKey().equals(entity.getKey())) {
            throw new IllegalArgumentException("key : <" + theOne.getKey() + "> can not be modified");
        }
        ruleGroupRepository.save(entity);
    }

    public void delete(Long id) {
        RuleGroupDefinition entity = ruleGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id: <" + id + "> not found"));
        if (!entity.getEnabled()) {
            throw new IllegalArgumentException("id: <" + id + "> is disabled already");
        }
        entity.setEnabled(false);
    }

    public Page<RuleGroupRes> findAll(RuleGroupPageReq pageReq) {
        RuleGroupDefinition condition = new RuleGroupDefinition();
        BeanUtils.copyProperties(pageReq, condition);

        return ruleGroupRepository.findAll(this.condition(condition), pageReq.build())
                .map(entity -> {
                    RuleGroupRes res = new RuleGroupRes();
                    BeanUtils.copyProperties(entity, res);
                    return res;
                });
    }

    public Map<String, String> getMap() {
        RuleGroupDefinition condition = new RuleGroupDefinition();
        condition.setEnabled(true);
        List<RuleGroupDefinition> list = ruleGroupRepository.findAll(this.condition(condition));
        return list.stream().collect(Collectors.toMap(RuleGroupDefinition::getKey, RuleGroupDefinition::getName));
    }

    private Specification<RuleGroupDefinition> condition(RuleGroupDefinition condition) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if (condition.getId() != null) {
                list.add(criteriaBuilder.equal(root.get("id").as(Long.class), condition.getId()));
            }
            if (condition.getKey() != null) {
                list.add(criteriaBuilder.equal(root.get("key").as(String.class), condition.getKey()));
            }
            if (condition.getName() != null) {
                list.add(criteriaBuilder.like(root.get("name").as(String.class), condition.getName() + "%"));
            }
            if (condition.getType() != null) {
                list.add(criteriaBuilder.equal(root.get("type").as(String.class), condition.getType()));
            }
            if (condition.getEnabled() != null) {
                list.add(criteriaBuilder.equal(root.get("enabled").as(Boolean.class), condition.getEnabled()));
            }

            return criteriaBuilder.and(list.toArray(new Predicate[0]));
        };
    }

    @Autowired
    public void setRuleGroupRepository(RuleGroupRepository ruleGroupRepository) {
        this.ruleGroupRepository = ruleGroupRepository;
    }
}
