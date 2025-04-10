package com.alatka.rule.admin.service;


import com.alatka.rule.admin.entity.RuleDatasourceDefinition;
import com.alatka.rule.admin.model.ruledatasource.RuleDatasourcePageReq;
import com.alatka.rule.admin.model.ruledatasource.RuleDatasourceReq;
import com.alatka.rule.admin.model.ruledatasource.RuleDatasourceRes;
import com.alatka.rule.admin.repository.RuleDatasourceRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class RuleDatasourceService {

    private RuleDatasourceRepository ruleDatasourceRepository;

    private RuleDatasourceExtService ruleDatasourceExtService;

    public Long create(RuleDatasourceReq req) {
        RuleDatasourceDefinition entity = new RuleDatasourceDefinition();
        BeanUtils.copyProperties(req, entity);

        RuleDatasourceDefinition condition = new RuleDatasourceDefinition();
        condition.setKey(entity.getKey());
        condition.setGroupKey(entity.getGroupKey());
        boolean exists = ruleDatasourceRepository.exists(this.condition(condition));
        if (exists) {
            throw new IllegalArgumentException("key : <" + condition.getKey() + "> is present already");
        }

        Long id = ruleDatasourceRepository.save(entity).getId();
        ruleDatasourceExtService.save(req.getExtended(), id, entity.getGroupKey());
        return id;
    }

    public void update(RuleDatasourceReq req) {
        boolean exists = ruleDatasourceRepository.existsById(req.getId());
        if (!exists) {
            throw new IllegalArgumentException("id : <" + req.getId() + "> not found");
        }

        RuleDatasourceDefinition entity = new RuleDatasourceDefinition();
        BeanUtils.copyProperties(req, entity);
        ruleDatasourceRepository.save(entity);
        ruleDatasourceExtService.deleteByDatasourceId(entity.getId());
        ruleDatasourceExtService.save(req.getExtended(), entity.getId(), entity.getGroupKey());
    }

    public void delete(Long id) {
        RuleDatasourceDefinition entity = ruleDatasourceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id: <" + id + "> not found"));
        ruleDatasourceRepository.delete(entity);
        ruleDatasourceExtService.deleteByDatasourceId(id);
    }

    public Page<RuleDatasourceRes> queryPage(RuleDatasourcePageReq pageReq) {
        RuleDatasourceDefinition condition = new RuleDatasourceDefinition();
        BeanUtils.copyProperties(pageReq, condition);

        return ruleDatasourceRepository.findAll(this.condition(condition), pageReq.build())
                .map(entity -> {
                    RuleDatasourceRes res = new RuleDatasourceRes();
                    BeanUtils.copyProperties(entity, res);
                    Map<String, String> extended = ruleDatasourceExtService.queryByDatasourceId(entity.getId()).stream()
                            .collect(HashMap::new, (k, v) -> k.put(v.getKey(), v.getValue()), HashMap::putAll);
                    res.setExtended(extended);
                    return res;
                });
    }

    private Specification<RuleDatasourceDefinition> condition(RuleDatasourceDefinition condition) {
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
            if (condition.getType() != null) {
                list.add(criteriaBuilder.equal(root.get("type").as(String.class), condition.getType()));
            }
            if (condition.getScope() != null) {
                list.add(criteriaBuilder.equal(root.get("scope").as(String.class), condition.getScope()));
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
    public void setRuleDatasourceRepository(RuleDatasourceRepository ruleDatasourceRepository) {
        this.ruleDatasourceRepository = ruleDatasourceRepository;
    }

    @Autowired
    public void setRuleDatasourceExtService(RuleDatasourceExtService ruleDatasourceExtService) {
        this.ruleDatasourceExtService = ruleDatasourceExtService;
    }
}
