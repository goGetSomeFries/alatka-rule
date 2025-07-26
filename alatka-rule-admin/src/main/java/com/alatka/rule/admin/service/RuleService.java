package com.alatka.rule.admin.service;


import com.alatka.rule.admin.AutoConfiguration;
import com.alatka.rule.admin.entity.RuleDefinition;
import com.alatka.rule.admin.entity.RuleExtDefinition;
import com.alatka.rule.admin.model.rule.RuleBuildReq;
import com.alatka.rule.admin.model.rule.RulePageReq;
import com.alatka.rule.admin.model.rule.RuleReq;
import com.alatka.rule.admin.model.rule.RuleRes;
import com.alatka.rule.admin.repository.RuleRepository;
import com.alatka.rule.core.RuleEngine;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Transactional
public class RuleService {

    private RuleRepository ruleRepository;

    private RuleExtService ruleExtService;

    private RuleUnitService ruleUnitService;

    private RestTemplate restTemplate;

    private TaskExecutor taskExecutor;

    private final RuleEngine ruleEngine = new RuleEngine();

    public void validate(String expression) {
        ruleEngine.validate(expression);
    }

    public Map<String, String> getType() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("1", "规则");
        map.put("2", "黑名单");
        map.put("3", "白名单");
        return map;
    }

    public Map<String, String> build(RuleBuildReq ruleBuildReq) {
        List<String> uriList = ruleBuildReq.getUris();
        Map<String, String> resultMap = new HashMap<>(uriList.size());

        CompletableFuture<Void> completableFuture = uriList.stream()
                .map(uri -> uri.concat(ruleBuildReq.getPath()))
                .map(url -> this.doBuild(url, resultMap))
                .collect(Collectors.collectingAndThen(Collectors.toList(),
                        list -> CompletableFuture.allOf(list.toArray(new CompletableFuture[0]))));
        completableFuture.join();
        return resultMap;
    }

    private CompletableFuture<Void> doBuild(String url, Map<String, String> resultMap) {
        return CompletableFuture.supplyAsync(() -> restTemplate.postForObject(url, null, String.class), taskExecutor)
                .handleAsync((result, ex) -> "ok".equalsIgnoreCase(result) ? "success" : "fail")
                .thenAccept(result -> resultMap.put(url, result));
    }

    public Long create(RuleReq req) {
        RuleDefinition entity = new RuleDefinition();
        BeanUtils.copyProperties(req, entity);

        Long ruleId = ruleRepository.save(entity).getId();
        ruleExtService.save(req.getExtended(), ruleId, entity.getGroupKey());
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
        ruleExtService.deleteByRuleId(entity.getId());
        ruleExtService.save(req.getExtended(), entity.getId(), entity.getGroupKey());
    }

    public void delete(Long id) {
        RuleDefinition entity = ruleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("rule id: " + id + " not found"));
        ruleRepository.delete(entity);

        ruleExtService.deleteByRuleId(id);

        ruleUnitService.deleteByRuleId(id);
    }

    public Page<RuleRes> queryPage(RulePageReq pageReq) {
        RuleDefinition condition = new RuleDefinition();
        BeanUtils.copyProperties(pageReq, condition);

        return ruleRepository.findAll(this.condition(condition), pageReq.build())
                .map(entity -> {
                    RuleRes res = new RuleRes();
                    BeanUtils.copyProperties(entity, res);
                    Map<String, Object> extended = ruleExtService.queryByRuleId(entity.getId()).stream()
                            .sorted(Comparator.comparing(RuleExtDefinition::getKey))
                            .collect(LinkedHashMap::new, (k, v) -> k.put(v.getKey(), v.getValue()), LinkedHashMap::putAll);
                    res.setExtended(extended);
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
            if (condition.getType() != null) {
                list.add(criteriaBuilder.equal(root.get("type").as(String.class), condition.getType()));
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
    public void setRuleExtService(RuleExtService ruleExtService) {
        this.ruleExtService = ruleExtService;
    }

    @Autowired
    public void setRuleUnitService(RuleUnitService ruleUnitService) {
        this.ruleUnitService = ruleUnitService;
    }

    @Autowired
    @Qualifier(AutoConfiguration.REST_TEMPLATE_NAME)
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    @Qualifier(AutoConfiguration.TASK_EXECUTOR_NAME)
    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }
}
