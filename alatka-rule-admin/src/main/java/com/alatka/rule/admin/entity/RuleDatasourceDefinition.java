package com.alatka.rule.admin.entity;

import javax.persistence.*;

@Entity
@Table(name = "ALK_RULE_DATASOURCE_DEFINITION")
public class RuleDatasourceDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "D_ID")
    private Long id;

    @Column(name = "D_KEY")
    private String key;

    @Column(name = "D_NAME")
    private String name;

    @Column(name = "D_TYPE")
    private String type;

    @Column(name = "D_SCOPE")
    private String scope;

    @Column(name = "D_ENABLED")
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
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
