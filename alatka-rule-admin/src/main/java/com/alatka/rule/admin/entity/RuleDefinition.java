package com.alatka.rule.admin.entity;

import javax.persistence.*;

@Entity
@Table(name = "ALK_RULE_DEFINITION")
public class RuleDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "R_ID")
    private Long id;

    @Column(name = "R_TYPE")
    private String type;

    @Column(name = "R_NAME")
    private String name;

    @Column(name = "R_DESC")
    private String desc;

    @Column(name = "R_PRIORITY")
    private Integer priority;

    @Column(name = "R_SCORE")
    private Integer score;

    @Column(name = "R_ORDER")
    private Integer order;

    @Column(name = "R_ENABLED")
    private Boolean enabled;

    @Column(name = "G_KEY")
    private String groupKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
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