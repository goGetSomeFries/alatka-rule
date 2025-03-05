package com.alatka.rule.admin.model.rule;

import com.alatka.rule.admin.model.PageReqMessage;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;

@Schema(description = "规则请求")
public class RulePageReq extends PageReqMessage {

    @Schema(description = "类型")
    private String type;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "描述")
    private String desc;

    @Schema(description = "是否可用")
    private Boolean enabled;

    @Schema(description = "规则组关键字", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty
    private String groupKey;

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
