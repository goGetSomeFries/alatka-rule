package com.alatka.rule.admin.model.rule;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Schema(description = "规则请求")
public class RuleReq {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "type 不能为空")
    private String type;

    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "name 不能为空")
    private String name;

    @Schema(description = "描述", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "desc 不能为空")
    private String desc;

    @Schema(description = "优先级", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "priority 不能为空")
    private Integer priority;

    @Schema(description = "评分", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "score 不能为空")
    private Integer score;

    @Schema(description = "顺序", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "order 不能为空")
    private Integer order;

    @Schema(description = "是否可用", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "enabled 不能为空")
    private Boolean enabled;

    @Schema(description = "规则组关键字", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "groupKey 不能为空")
    private String groupKey;

    @Schema(description = "扩展属性")
    private Map<String, Object> extended = new HashMap<>();

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

    public Map<String, Object> getExtended() {
        return extended;
    }

    @JsonAnySetter
    public void setExtended(String key, Object value) {
        this.extended.put(key, value);
    }
}
