package com.zch.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 放行浏览器的 OPTIONS 跨域预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 2. 从请求头里拿到前端塞进来的 Token
        String token = request.getHeader("Authorization");

        // 3. 去 Redis 里查有没有这个 Token
        if (token != null && Boolean.TRUE.equals(redisTemplate.hasKey("TOKEN:" + token))) {
            // Token 存在且合法！顺手帮用户延长一下 2 小时的过期时间（活跃用户永不掉线）
            redisTemplate.expire("TOKEN:" + token, 2, TimeUnit.HOURS);
            return true; // 准许通行！
        }

        // 4. 如果没有 Token 或者过期了，直接打回 401 错误码
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write("{\"code\":401, \"message\":\"未登录或Token已过期\"}");
        return false; // 拦截！不让你访问后面的 Controller
    }
}