package com.zch.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    /**
     * 测试鉴权拦截器的专用接口
     * 注意：这个接口没有在 spring-mvc.xml 中被 exclude（排除），
     * 所以必须携带合法的 Token 才能访问到这里！
     */
    @GetMapping("/test")
    public Map<String, Object> hello() {
        Map<String, Object> result = new HashMap<>();
        // 模拟标准的 JSON 返回格式
        result.put("code", 200);
        result.put("message", "鉴权通过！后端成功接收到了合法的 Token！");
        result.put("data", "Hello World 2.0 - 鉴权测试版");
        return result;
    }
}