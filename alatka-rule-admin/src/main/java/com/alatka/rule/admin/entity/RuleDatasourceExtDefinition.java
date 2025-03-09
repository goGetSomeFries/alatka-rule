package com.alatka.rule.admin.entity;

import javax.persistence.*;

@Entity
@Table(name = "ALK_RULE_DATASOURCE_EXT_DEFINITION")
public class RuleDatasourceExtDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "S_ID")
    private Long id;

    @Column(name = "S_KEY")
    private String key;

    @Column(name = "S_VALUE")
    private String value;

    @Column(name = "D_ID")
    private Long datasourceId;

    @Column(name = "G_KEY")
    private String groupKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(Long datasourceId) {
        this.datasourceId = datasourceId;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }
}
