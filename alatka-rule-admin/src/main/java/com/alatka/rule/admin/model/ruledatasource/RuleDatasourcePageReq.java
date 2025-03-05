package com.alatka.rule.admin.model.ruledatasource;

import com.alatka.rule.admin.model.PageReqMessage;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "规则外部数据源请求")
public class RuleDatasourcePageReq extends PageReqMessage {
    @Schema(description = "关键字")
    private String key;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "类型")
    private String type;

    @Schema(description = "数据范围")
    private String scope;

    @Schema(description = "是否可用")
    private Boolean enabled;

    @Schema(description = "规则组关键字")
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
