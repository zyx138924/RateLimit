package com.zyx.controller;

import com.zyx.annotation.RateLimit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ratelimit")
public class ApiController {

    @RateLimit("2")
    @GetMapping
    public String getString() {
        return "success";
    }
}
