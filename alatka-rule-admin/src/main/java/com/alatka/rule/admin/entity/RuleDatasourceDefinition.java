package com.alatka.rule.admin.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ALK_RULE_DATASOURCE_DEFINITION")
@EntityListeners(AuditingEntityListener.class)
public class RuleDatasourceDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "D_ID")
    private Long id;

    @CreatedBy
    @Column(name = "D_CREATE_BY", updatable = false)
    private String createBy;

    @CreatedDate
    @Column(name = "D_CREATE_AT", updatable = false)
    private LocalDateTime createAt;

    @LastModifiedBy
    @Column(name = "D_UPDATE_BY", insertable = false)
    private String updateBy;

    @LastModifiedDate
    @Column(name = "D_UPDATE_AT", insertable = false)
    private LocalDateTime updateAt;

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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
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
