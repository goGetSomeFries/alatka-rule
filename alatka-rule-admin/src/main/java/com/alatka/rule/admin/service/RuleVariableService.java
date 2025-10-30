package com.alatka.rule.admin.service;


import com.alatka.rule.admin.entity.RuleVariableDefinition;
import com.alatka.rule.admin.model.rulevariable.RuleVariablePageReq;
import com.alatka.rule.admin.model.rulevariable.RuleVariableReq;
import com.alatka.rule.admin.model.rulevariable.RuleVariableRes;
import com.alatka.rule.admin.repository.RuleVariableRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class RuleVariableService {

    private RuleVariableRepository ruleVariableRepository;

    public Map<String, String> getType() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("PARAM", "预处理入参");
        map.put("TEMPLATE", "模板");
        map.put("VAR", "变量");
        map.put("OPE", "运算符");
        map.put("FUNC", "函数");
        return map;
    }

    public Long create(RuleVariableReq req) {
        RuleVariableDefinition entity = new RuleVariableDefinition();
        BeanUtils.copyProperties(req, entity);

        RuleVariableDefinition condition = new RuleVariableDefinition();
        condition.setKey(entity.getKey());
        condition.setGroupKey(entity.getGroupKey());
        condition.setType(entity.getType());
        boolean exists = ruleVariableRepository.exists(this.condition(condition));
        if (exists) {
            throw new IllegalArgumentException("key : <" + condition.getKey() + "> is present already");
        }

        return ruleVariableRepository.save(entity).getId();
    }

    public void update(RuleVariableReq req) {
        RuleVariableDefinition entity = new RuleVariableDefinition();
        BeanUtils.copyProperties(req, entity);

        boolean exists = ruleVariableRepository.existsById(entity.getId());
        if (!exists) {
            throw new IllegalArgumentException("id : <" + entity.getId() + "> not found");
        }
        ruleVariableRepository.save(entity);
    }

    public void delete(Long id) {
        RuleVariableDefinition entity = ruleVariableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id: <" + id + "> not found"));
        ruleVariableRepository.delete(entity);
    }

    public Page<RuleVariableRes> queryPage(RuleVariablePageReq pageReq) {
        RuleVariableDefinition condition = new RuleVariableDefinition();
        BeanUtils.copyProperties(pageReq, condition);

        return ruleVariableRepository.findAll(this.condition(condition), pageReq.build())
                .map(entity -> {
                    RuleVariableRes res = new RuleVariableRes();
                    BeanUtils.copyProperties(entity, res);
                    return res;
                });
    }

    public List<RuleVariableRes> queryByType(String groupKey, String type) {
        RuleVariableDefinition condition = new RuleVariableDefinition();
        condition.setType(type);
        condition.setGroupKey(groupKey);
        condition.setEnabled(true);
        List<RuleVariableDefinition> list = ruleVariableRepository.findAll(this.condition(condition));

        if ("VAR".equals(type)) {
            condition.setType("PARAM");
            Set<String> set = ruleVariableRepository.findAll(this.condition(condition))
                    .stream().map(RuleVariableDefinition::getKey).collect(Collectors.toSet());
            list.removeIf(entity -> set.contains(entity.getKey()));
        }

        return list.stream()
                .sorted(Comparator.comparing(RuleVariableDefinition::getOrder))
                .map(entity -> {
                    RuleVariableRes res = new RuleVariableRes();
                    BeanUtils.copyProperties(entity, res);
                    return res;
                }).collect(Collectors.toList());
    }

    private Specification<RuleVariableDefinition> condition(RuleVariableDefinition condition) {
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
            if (condition.getDesc() != null) {
                list.add(criteriaBuilder.like(root.get("desc").as(String.class), "%" + condition.getDesc() + "%"));
            }
            if (condition.getType() != null) {
                list.add(criteriaBuilder.equal(root.get("type").as(String.class), condition.getType()));
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
    public void setRuleVariableRepository(RuleVariableRepository ruleVariableRepository) {
        this.ruleVariableRepository = ruleVariableRepository;
    }
}
