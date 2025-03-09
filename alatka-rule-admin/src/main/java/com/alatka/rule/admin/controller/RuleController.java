package com.alatka.rule.admin.controller;

import com.alatka.rule.admin.model.PageResMessage;
import com.alatka.rule.admin.model.ResMessage;
import com.alatka.rule.admin.model.rule.RuleBuildReq;
import com.alatka.rule.admin.model.rule.RulePageReq;
import com.alatka.rule.admin.model.rule.RuleReq;
import com.alatka.rule.admin.model.rule.RuleRes;
import com.alatka.rule.admin.service.RuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Tag(name = "规则")
@RestController
@RequestMapping("/rule")
public class RuleController {

    private RuleService ruleService;

    @Operation(summary = "规则表达式校验")
    @Parameter(name = "expression", description = "规则表达式", required = true)
    @PutMapping("/validate")
    public ResMessage<Void> validate(@Valid @RequestBody String expression) {
        return ResMessage.success(() -> ruleService.validate(expression));
    }

    @Operation(summary = "部署/回退规则")
    @PostMapping("/build")
    public ResMessage<Map<String, String>> build(@Valid @RequestBody RuleBuildReq ruleBuildReq) {
        return ResMessage.success(ruleService.build(ruleBuildReq));
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
}
