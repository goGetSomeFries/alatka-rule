package com.alatka.rule.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "前端页面跳转")
@Controller
@RequestMapping("/")
public class HtmlController {

    @Operation(summary = "规则组")
    @GetMapping("/rule/group")
    public String group() {
        return "group";
    }

    @Operation(summary = "规则外部数据源")
    @GetMapping("/rule/datasource")
    public String datasource() {
        return "datasource";
    }

    @Operation(summary = "规则预处理参数")
    @GetMapping("/rule/param")
    public String param() {
        return "param";
    }

    @Operation(summary = "规则")
    @GetMapping("/rule")
    public String rule() {
        return "rule";
    }
}
