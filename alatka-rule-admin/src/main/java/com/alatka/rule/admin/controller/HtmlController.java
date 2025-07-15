package com.alatka.rule.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HtmlController {

    @GetMapping("/rule/group")
    public String group() {
        return "group";
    }

    @GetMapping("/rule/datasource")
    public String datasource() {
        return "datasource";
    }

    @GetMapping("/rule/param")
    public String param() {
        return "param";
    }
}
