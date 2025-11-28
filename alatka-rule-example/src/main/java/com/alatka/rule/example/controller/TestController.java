package com.alatka.rule.example.controller;

import com.alatka.rule.admin.model.ResMessage;
import com.alatka.rule.core.RuleEngine;
import com.alatka.rule.core.definition.RuleDefinitionBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Tag(name = "规则")
@RestController
@RequestMapping("/")
public class TestController {

    private RuleEngine ruleEngine;

    private RuleDefinitionBuilder ruleDefinitionBuilder;

    @Operation(summary = "执行规则")
    @PostMapping("/rule/execute")
    public ResMessage<Void> execute(@Valid @RequestBody Map<String, Object> params) {
        return ResMessage.success(() -> ruleEngine.execute("med", params));
    }

    @Operation(summary = "执行构建")
    @PostMapping("/rule/deploy")
    public String execute(@Valid @RequestBody List<String> groups) {
        ruleDefinitionBuilder.refresh(groups.toArray(new String[0]));
        return "ok";
    }

    @Autowired
    public void setRuleEngine(RuleEngine ruleEngine) {
        this.ruleEngine = ruleEngine;
    }

    @Autowired
    public void setRuleDefinitionBuilder(RuleDefinitionBuilder ruleDefinitionBuilder) {
        this.ruleDefinitionBuilder = ruleDefinitionBuilder;
    }
}
