package com.alatka.rule.admin.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ALK_RULE_VARIABLE_DEFINITION")
public class RuleVariableDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "V_ID")
    private Long id;

    @Column(name = "V_KEY")
    private String key;

    @Column(name = "V_NAME")
    private String name;

    @Column(name = "V_DESC")
    private String desc;

    @Column(name = "V_TYPE")
    private String type;

    @Column(name = "V_ORDER")
    private Integer order;

    @Column(name = "V_ENABLED")
    private Boolean enabled;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }
}