package com.alatka.rule.admin.controller;

import com.alatka.rule.admin.model.ResMessage;
import com.alatka.rule.admin.model.ruleunit.RuleUnitReq;
import com.alatka.rule.admin.model.ruleunit.RuleUnitRes;
import com.alatka.rule.admin.service.RuleUnitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "规则单元")
@RestController
@RequestMapping("/rule/unit")
public class RuleUnitController {

    private RuleUnitService ruleUnitService;

    @Operation(summary = "创建规则单元")
    @PostMapping("/create")
    public ResMessage<Long> create(@Valid @RequestBody RuleUnitReq ruleUnitReq) {
        return ResMessage.success(ruleUnitService.create(ruleUnitReq));
    }

    @Operation(summary = "删除规则单元")
    @Parameter(name = "id", description = "编号", required = true)
    @DeleteMapping("/delete")
    public ResMessage<Void> delete(@RequestParam Long id) {
        return ResMessage.success(() -> ruleUnitService.delete(id));
    }

    @Operation(summary = "修改规则单元")
    @PutMapping("/update")
    public ResMessage<Void> update(@Valid @RequestBody RuleUnitReq ruleUnitReq) {
        return ResMessage.success(() -> ruleUnitService.update(ruleUnitReq));
    }

    @Operation(summary = "查询规则单元")
    @Parameter(name = "ruleId", description = "规则编号", required = true)
    @GetMapping("/list")
    public ResMessage<List<RuleUnitRes>> list(@RequestParam Long ruleId) {
        return ResMessage.success(ruleUnitService.queryByRuleId(ruleId));
    }

    @Autowired
    public void setRuleUnitService(RuleUnitService ruleUnitService) {
        this.ruleUnitService = ruleUnitService;
    }

}
