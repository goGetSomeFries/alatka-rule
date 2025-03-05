package com.alatka.rule.admin.model.ruleunit;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(description = "规则单元请求")
public class RuleUnitReq {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "表达式", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty
    private String expression;

    @Schema(description = "顺序", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer order;

    @Schema(description = "是否可用", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Boolean enabled;

    @Schema(description = "外部数据源标识")
    private String datasourceKey;

    @Schema(description = "规则主键", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Long ruleId;

    @Schema(description = "规则组标识", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty
    private String groupKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
