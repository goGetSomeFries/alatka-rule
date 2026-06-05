package com.alatka.rule.admin.model.ruleunit;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "规则单元响应")
public class RuleUnitRes {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "创建人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createAt;

    @Schema(description = "更新人")
    private String updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新日期")
    private LocalDateTime updateAt;

    @Schema(description = "表达式", requiredMode = Schema.RequiredMode.REQUIRED)
    private String expression;

    @Schema(description = "顺序", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer order;

    @Schema(description = "是否可用", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean enabled;

    @Schema(description = "外部数据源标识")
    private String datasourceKey;

    @Schema(description = "规则主键", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long ruleId;

    @Schema(description = "规则组标识", requiredMode = Schema.RequiredMode.REQUIRED)
    private String groupKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
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

    public String getDatasourceKey() {
        return datasourceKey;
    }

    public void setDatasourceKey(String datasourceKey) {
        this.datasourceKey = datasourceKey;
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
