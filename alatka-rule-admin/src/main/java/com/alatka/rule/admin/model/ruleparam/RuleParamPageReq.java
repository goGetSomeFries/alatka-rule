package com.alatka.rule.admin.model.ruleparam;

import com.alatka.rule.admin.model.PageReqMessage;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;

@Schema(description = "规则入参请求")
public class RuleParamPageReq extends PageReqMessage {

    @Schema(description = "关键字")
    private String key;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "表达式")
    private String expression;

    @Schema(description = "是否可用")
    private Boolean enabled;

    @Schema(description = "规则组关键字", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "groupKey 不能为空")
    private String groupKey;

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

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
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
