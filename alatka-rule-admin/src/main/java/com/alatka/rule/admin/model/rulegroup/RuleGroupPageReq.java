package com.alatka.rule.admin.model.rulegroup;

import com.alatka.rule.admin.model.PageReqMessage;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "规则组请求")
public class RuleGroupPageReq extends PageReqMessage {

    @Schema(description = "关键字")
    private String key;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "类型")
    private String type;

    @Schema(description = "是否可用")
    private Boolean enabled;

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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
