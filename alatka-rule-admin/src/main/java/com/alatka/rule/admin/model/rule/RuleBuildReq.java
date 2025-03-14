package com.alatka.rule.admin.model.rule;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Schema(description = "规则部署/回退请求")
public class RuleBuildReq {

    @Schema(description = "uri集合", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty
    private List<String> uris;

    @Schema(description = "路径", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty
    private String path;

    public List<String> getUris() {
        return uris;
    }

    public void setUris(List<String> uris) {
        this.uris = uris;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
