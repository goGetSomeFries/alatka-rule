package com.alatka.rule.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class Test1Controller {

    @GetMapping("/index")
    public String build() {
        return "index";
    }
}
