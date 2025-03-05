package com.alatka.rule.admin.controller;

import com.alatka.rule.admin.model.PageResMessage;
import com.alatka.rule.admin.model.ResMessage;
import com.alatka.rule.admin.model.rule.RulePageReq;
import com.alatka.rule.admin.model.rule.RuleReq;
import com.alatka.rule.admin.model.rule.RuleRes;
import com.alatka.rule.admin.service.RuleService;
import com.alatka.rule.core.definition.RuleDefinitionBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "规则")
@RestController
@RequestMapping("/rule")
public class RuleController {

    private RuleService ruleService;

    private RuleDefinitionBuilder ruleDefinitionBuilder;

    @Operation(summary = "部署规则")
    @PostMapping("/build")
    public ResMessage<Void> build() {
        return ResMessage.success(() -> ruleDefinitionBuilder.build());
    }

    @Operation(summary = "刷新规则")
    @PostMapping("/refresh")
    public ResMessage<Void> refresh() {
        return ResMessage.success(() -> ruleDefinitionBuilder.refresh());
    }

    @Operation(summary = "回退规则")
    @PostMapping("/fallback")
    public ResMessage<Void> fallback() {
        return ResMessage.success(() -> ruleDefinitionBuilder.fallback());
    }

    @Operation(summary = "创建规则")
    @PostMapping("/create")
    public ResMessage<Long> create(@Valid @RequestBody RuleReq ruleReq) {
        return ResMessage.success(ruleService.create(ruleReq));
    }

    @Operation(summary = "删除规则")
    @Parameter(name = "id", description = "编号", required = true)
    @DeleteMapping("/delete")
    public ResMessage<Void> delete(@RequestParam Long id) {
        return ResMessage.success(() -> ruleService.delete(id));
    }

    @Operation(summary = "修改规则")
    @PutMapping("/update")
    public ResMessage<Void> update(@Valid @RequestBody RuleReq ruleReq) {
        return ResMessage.success(() -> ruleService.update(ruleReq));
    }

    @Operation(summary = "分页查询规则")
    @GetMapping("/page")
    public PageResMessage<RuleRes> queryPage(@Valid RulePageReq rulePageReq) {
        return PageResMessage.success(ruleService.queryPage(rulePageReq));
    }

    @Autowired
    public void setRuleService(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    @Autowired(required = false)
    public void setRuleDefinitionBuilder(RuleDefinitionBuilder ruleDefinitionBuilder) {
        this.ruleDefinitionBuilder = ruleDefinitionBuilder;
    }

}
