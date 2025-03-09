package com.alatka.rule.admin.entity;

import javax.persistence.*;

@Entity
@Table(name = "ALK_RULE_EXT_DEFINITION")
public class RuleExtDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "E_ID")
    private Long id;

    @Column(name = "E_KEY")
    private String key;

    @Column(name = "E_VALUE")
    private String value;

    @Column(name = "R_ID")
    private Long ruleId;

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

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }
}