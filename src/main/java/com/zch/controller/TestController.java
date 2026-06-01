package com.zch.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
@RestController
public class TestController {

    @GetMapping({"/", "/test"})
    public Map<String, Object> hello() {
        Map<String, Object> result = new HashMap<>();
        // 模拟标准的 JSON 返回格式
        result.put("code", 200);
        result.put("data", "Hello World");
        return result;
    }
}