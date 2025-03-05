package com.alatka.rule.admin.service;


import com.alatka.rule.admin.entity.RuleDatasourceExtDefinition;
import com.alatka.rule.admin.repository.RuleDatasourceExtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class RuleDatasourceExtService {

    private RuleDatasourceExtRepository ruleDatasourceExtRepository;

    public void save(Map<String, Object> extended, Long datasourceId, String groupKey) {
        List<RuleDatasourceExtDefinition> extendedList = extended.entrySet().stream().map(entry -> {
            RuleDatasourceExtDefinition entity = new RuleDatasourceExtDefinition();
            entity.setDatasourceId(datasourceId);
            entity.setGroupKey(groupKey);
            entity.setKey(entry.getKey());
            entity.setValue(entry.getValue() == null ? null : entry.getValue().toString());
            return entity;
        }).collect(Collectors.toList());
        ruleDatasourceExtRepository.saveAll(extendedList);
    }

    public void deleteByDatasourceId(Long datasourceId) {
        List<RuleDatasourceExtDefinition> list = this.queryByDatasourceId(datasourceId);
        List<Long> ids = list.stream().map(RuleDatasourceExtDefinition::getId).collect(Collectors.toList());
        ruleDatasourceExtRepository.deleteAllById(ids);
    }

    public List<RuleDatasourceExtDefinition> queryByDatasourceId(Long datasourceId) {
        RuleDatasourceExtDefinition entity = new RuleDatasourceExtDefinition();
        entity.setDatasourceId(datasourceId);
        return ruleDatasourceExtRepository.findAll(Example.of(entity));
    }

    @Autowired
    public void setRuleDatasourceExtRepository(RuleDatasourceExtRepository ruleDatasourceExtRepository) {
        this.ruleDatasourceExtRepository = ruleDatasourceExtRepository;
    }
}
