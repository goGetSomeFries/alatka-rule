package com.alatka.rule.admin.controller;

import com.alatka.rule.admin.model.PageResMessage;
import com.alatka.rule.admin.model.ResMessage;
import com.alatka.rule.admin.model.ruledatasource.RuleDatasourcePageReq;
import com.alatka.rule.admin.model.rulevariable.RuleVariablePageReq;
import com.alatka.rule.admin.model.rulevariable.RuleVariableReq;
import com.alatka.rule.admin.model.rulevariable.RuleVariableRes;
import com.alatka.rule.admin.service.RuleDatasourceService;
import com.alatka.rule.admin.service.RuleVariableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "规则变量")
@RestController
@RequestMapping("/rule/variable")
public class RuleVariableController {

    private RuleVariableService ruleVariableService;

    private RuleDatasourceService ruleDatasourceService;

    @Operation(summary = "创建规则变量")
    @PostMapping("/create")
    public ResMessage<Long> create(@Valid @RequestBody RuleVariableReq ruleVariableReq) {
        return ResMessage.success(ruleVariableService.create(ruleVariableReq));
    }

    @Operation(summary = "删除规则变量")
    @Parameter(name = "id", description = "编号", required = true)
    @DeleteMapping("/delete")
    public ResMessage<Void> delete(@RequestParam Long id) {
        return ResMessage.success(() -> ruleVariableService.delete(id));
    }

    @Operation(summary = "修改规则变量")
    @PutMapping("/update")
    public ResMessage<Void> update(@Valid @RequestBody RuleVariableReq ruleVariableReq) {
        return ResMessage.success(() -> ruleVariableService.update(ruleVariableReq));
    }

    @Operation(summary = "分页查询规则变量")
    @GetMapping("/page")
    public PageResMessage<RuleVariableRes> queryPage(@Valid @ParameterObject RuleVariablePageReq ruleVariablePageReq) {
        return PageResMessage.success(ruleVariableService.queryPage(ruleVariablePageReq));
    }

    @Operation(summary = "查询规则变量")
    @Parameter(name = "groupKey", description = "规则组关键字", required = true)
    @Parameter(name = "type", description = "类型", required = true)
    @GetMapping("/list")
    public ResMessage<List<RuleVariableRes>> list(@RequestParam String groupKey, @RequestParam String type) {
        if ("DATASOURCE".equals(type)) {
            RuleDatasourcePageReq pageReq = new RuleDatasourcePageReq();
            pageReq.setGroupKey(groupKey);
            pageReq.setEnabled(true);
            pageReq.setPageNo(1);
            pageReq.setPageSize(Integer.MAX_VALUE);
            pageReq.setDirection("asc");
            pageReq.setOrderBy("id");

            List<RuleVariableRes> list = ruleDatasourceService.queryPage(pageReq).map(entity -> {
                RuleVariableRes res = new RuleVariableRes();
                res.setKey(entity.getKey());
                res.setName(entity.getName());
                res.setDesc("类型：" + entity.getType() + "，范围：" + entity.getScope());
                return res;
            }).toList();
            return ResMessage.success(list);
        }
        return PageResMessage.success(ruleVariableService.queryByType(groupKey, type));
    }

    @Operation(summary = "规则变量类型")
    @GetMapping("/type")
    public ResMessage<Map<String, String>> getType() {
        return ResMessage.success(ruleVariableService.getType());
    }

    @Autowired
    public void setRuleVariableService(RuleVariableService ruleVariableService) {
        this.ruleVariableService = ruleVariableService;
    }

    @Autowired
    public void setRuleDatasourceService(RuleDatasourceService ruleDatasourceService) {
        this.ruleDatasourceService = ruleDatasourceService;
    }
}
