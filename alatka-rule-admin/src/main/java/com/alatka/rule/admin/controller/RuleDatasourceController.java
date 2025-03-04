package com.alatka.rule.admin.controller;

import com.alatka.rule.admin.model.PageResMessage;
import com.alatka.rule.admin.model.ResMessage;
import com.alatka.rule.admin.model.ruledatasource.RuleDatasourcePageReq;
import com.alatka.rule.admin.model.ruledatasource.RuleDatasourceReq;
import com.alatka.rule.admin.model.ruledatasource.RuleDatasourceRes;
import com.alatka.rule.admin.service.RuleDatasourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "规则外部数据源")
@RestController
@RequestMapping("/rule/datasource")
public class RuleDatasourceController {

    private RuleDatasourceService ruleDatasourceService;

    @Operation(summary = "创建规则外部数据源")
    @PostMapping("/create")
    public ResMessage<Long> create(@Valid @RequestBody RuleDatasourceReq ruleDatasourceReq) {
        return ResMessage.success(ruleDatasourceService.save(ruleDatasourceReq));
    }

    @Operation(summary = "删除规则外部数据源")
    @Parameter(name = "id", description = "编号", required = true)
    @DeleteMapping("/delete")
    public ResMessage<Void> delete(@RequestParam Long id) {
        return ResMessage.success(() -> ruleDatasourceService.delete(id));
    }

    @Operation(summary = "修改规则外部数据源")
    @PutMapping("/update")
    public ResMessage<Void> update(@Valid @RequestBody RuleDatasourceReq ruleDatasourceReq) {
        return ResMessage.success(() -> ruleDatasourceService.update(ruleDatasourceReq));
    }

    @Operation(summary = "分页查询规则外部数据源")
    @GetMapping("/page")
    public PageResMessage<RuleDatasourceRes> queryPage(@Valid RuleDatasourcePageReq ruleDatasourcePageReq) {
        return PageResMessage.success(ruleDatasourceService.findAll(ruleDatasourcePageReq));
    }

    @Autowired
    public void setRuleDatasourceService(RuleDatasourceService ruleDatasourceService) {
        this.ruleDatasourceService = ruleDatasourceService;
    }

}
