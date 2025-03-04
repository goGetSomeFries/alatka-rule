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

    @Column(name = "D_PARAM_K1")
    private String paramK1;

    @Column(name = "D_PARAM_V1")
    private String paramV1;

    @Column(name = "D_PARAM_K2")
    private String paramK2;

    @Column(name = "D_PARAM_V2")
    private String paramV2;

    @Column(name = "D_PARAM_K3")
    private String paramK3;

    @Column(name = "D_PARAM_V3")
    private String paramV3;

    @Column(name = "D_PARAM_K4")
    private String paramK4;

    @Column(name = "D_PARAM_V4")
    private String paramV4;

    @Column(name = "D_PARAM_K5")
    private String paramK5;

    @Column(name = "D_PARAM_V5")
    private String paramV5;

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

    public String getParamK1() {
        return paramK1;
    }

    public void setParamK1(String paramK1) {
        this.paramK1 = paramK1;
    }

    public String getParamV1() {
        return paramV1;
    }

    public void setParamV1(String paramV1) {
        this.paramV1 = paramV1;
    }

    public String getParamK2() {
        return paramK2;
    }

    public void setParamK2(String paramK2) {
        this.paramK2 = paramK2;
    }

    public String getParamV2() {
        return paramV2;
    }

    public void setParamV2(String paramV2) {
        this.paramV2 = paramV2;
    }

    public String getParamK3() {
        return paramK3;
    }

    public void setParamK3(String paramK3) {
        this.paramK3 = paramK3;
    }

    public String getParamV3() {
        return paramV3;
    }

    public void setParamV3(String paramV3) {
        this.paramV3 = paramV3;
    }

    public String getParamK4() {
        return paramK4;
    }

    public void setParamK4(String paramK4) {
        this.paramK4 = paramK4;
    }

    public String getParamV4() {
        return paramV4;
    }

    public void setParamV4(String paramV4) {
        this.paramV4 = paramV4;
    }

    public String getParamK5() {
        return paramK5;
    }

    public void setParamK5(String paramK5) {
        this.paramK5 = paramK5;
    }

    public String getParamV5() {
        return paramV5;
    }

    public void setParamV5(String paramV5) {
        this.paramV5 = paramV5;
    }
}
