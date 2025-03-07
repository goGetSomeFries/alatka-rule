package com.alatka.rule.admin.controller;

import com.alatka.rule.admin.model.PageResMessage;
import com.alatka.rule.admin.model.ResMessage;
import com.alatka.rule.admin.model.rulegroup.RuleGroupPageReq;
import com.alatka.rule.admin.model.rulegroup.RuleGroupReq;
import com.alatka.rule.admin.model.rulegroup.RuleGroupRes;
import com.alatka.rule.admin.service.RuleGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Tag(name = "规则组")
@RestController
@RequestMapping("/rule/group")
public class RuleGroupController {

    private RuleGroupService ruleGroupService;

    @Operation(summary = "创建规则组")
    @PostMapping("/create")
    public ResMessage<Long> create(@Valid @RequestBody RuleGroupReq ruleGroupReq) {
        return ResMessage.success(ruleGroupService.create(ruleGroupReq));
    }

    @Operation(summary = "删除规则组")
    @Parameter(name = "id", description = "编号", required = true)
    @DeleteMapping("/delete")
    public ResMessage<Void> delete(@RequestParam Long id) {
        return ResMessage.success(() -> ruleGroupService.delete(id));
    }

    @Operation(summary = "修改规则组")
    @PutMapping("/update")
    public ResMessage<Void> update(@Valid @RequestBody RuleGroupReq ruleGroupReq) {
        return ResMessage.success(() -> ruleGroupService.update(ruleGroupReq));
    }

    @Operation(summary = "分页查询规则组")
    @GetMapping("/page")
    public PageResMessage<RuleGroupRes> queryPage(@Valid RuleGroupPageReq pageReqMessage) {
        return PageResMessage.success(ruleGroupService.queryPage(pageReqMessage));
    }

    @Operation(summary = "规则组kv")
    @GetMapping("/map")
    public ResMessage<Map<String, String>> getMap() {
        return ResMessage.success(ruleGroupService.getMap());
    }


    @Autowired
    public void setRuleGroupService(RuleGroupService ruleGroupService) {
        this.ruleGroupService = ruleGroupService;
    }

}
