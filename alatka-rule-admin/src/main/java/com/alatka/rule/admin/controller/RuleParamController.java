package com.alatka.rule.admin.controller;

import com.alatka.rule.admin.model.PageResMessage;
import com.alatka.rule.admin.model.ResMessage;
import com.alatka.rule.admin.model.ruleparam.RuleParamPageReq;
import com.alatka.rule.admin.model.ruleparam.RuleParamReq;
import com.alatka.rule.admin.model.ruleparam.RuleParamRes;
import com.alatka.rule.admin.service.RuleParamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "规则入参")
@RestController
@RequestMapping("/rule/param")
public class RuleParamController {

    private RuleParamService ruleParamService;

    @Operation(summary = "创建规则入参")
    @PostMapping("/create")
    public ResMessage<Long> create(@Valid @RequestBody RuleParamReq ruleParamReq) {
        return ResMessage.success(ruleParamService.save(ruleParamReq));
    }

    @Operation(summary = "删除规则组")
    @Parameter(name = "id", description = "编号", required = true)
    @DeleteMapping("/delete")
    public ResMessage<Void> delete(@RequestParam Long id) {
        return ResMessage.success(() -> ruleParamService.delete(id));
    }

    @Operation(summary = "修改规则组")
    @PutMapping("/update")
    public ResMessage<Void> update(@Valid @RequestBody RuleParamReq ruleParamReq) {
        return ResMessage.success(() -> ruleParamService.update(ruleParamReq));
    }

    @Operation(summary = "分页查询规则组")
    @GetMapping("/page")
    public PageResMessage<RuleParamRes> queryPage(@Valid RuleParamPageReq ruleParamPageReq) {
        return PageResMessage.success(ruleParamService.findAll(ruleParamPageReq));
    }

    @Autowired
    public void setRuleParamService(RuleParamService ruleParamService) {
        this.ruleParamService = ruleParamService;
    }

}
