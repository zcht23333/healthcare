package com.zch.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.zch.mapper.SysAdminMapper;
import com.zch.entity.SysAdmin;

@RestController
public class LoginController {

    // 注入我们在 spring-redis.xml 中配置的 RedisTemplate
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 获取图形验证码接口
     */
    @GetMapping("/captcha")
    public Map<String, Object> getCaptcha() {
        // 1. 使用 Hutool 生成图形验证码 (宽:120, 高:40, 字符数:4, 干扰线:10)
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(120, 40, 4, 10);

        // 2. 获取验证码图片里的真实文字（比如 "A3B9"）
        String code = lineCaptcha.getCode();

        // 3. 生成一个唯一的 UUID，作为这个验证码在 Redis 里的“身份证号”
        String uuid = IdUtil.simpleUUID();
        String redisKey = "CAPTCHA:" + uuid;

        // 4. 将验证码答案存入 Redis，并设置过期时间为 3 分钟！
        // 这样前端传过来验证码时，我们就能根据 UUID 去 Redis 里找真正的答案进行比对
        redisTemplate.opsForValue().set(redisKey, code, 3, TimeUnit.MINUTES);

        // 5. 组装返回给前端的数据
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        // 返回 UUID（前端登录时需要带着这个 UUID 一起传给后端）
        data.put("uuid", uuid);
        // 返回图片的 Base64 编码（前端直接把这串代码塞进 <img> 标签的 src 就能显示图片）
        data.put("img", lineCaptcha.getImageBase64Data());

        result.put("code", 200);
        result.put("message", "验证码生成成功");
        result.put("data", data);

        return result;
    }

    @Autowired
    private SysAdminMapper sysAdminMapper;

    /**
     * 提交登录接口
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginForm) {
        Map<String, Object> result = new HashMap<>();

        // 1. 提取前端传来的参数
        String username = loginForm.get("username");
        String password = loginForm.get("password");
        String code = loginForm.get("code");
        String uuid = loginForm.get("uuid");

        // 2. 校验验证码
        String redisKey = "CAPTCHA:" + uuid;
        String realCode = redisTemplate.opsForValue().get(redisKey);

        if (realCode == null || !realCode.equalsIgnoreCase(code)) {
            result.put("code", 400);
            result.put("message", "验证码错误或已过期");
            return result;
        }

        // 3. 查数据库校验账号密码
        SysAdmin admin = sysAdminMapper.loginCheck(username, password);
        if (admin == null) {
            result.put("code", 400);
            result.put("message", "账号或密码错误");
            return result;
        }

        // 4. 登录成功！生成 Token 存入 Redis (设置2小时过期)
        String token = IdUtil.fastSimpleUUID();
        // 将 Token 作为 Key，用户名作为 Value 存入 Redis
        redisTemplate.opsForValue().set("TOKEN:" + token, username, 2, TimeUnit.HOURS);

        // 5. 阅后即焚：验证码一旦使用成功，立刻从 Redis 中删除，防止重复利用
        redisTemplate.delete(redisKey);

        // 6. 返回 Token 给前端
        result.put("code", 200);
        result.put("message", "登录成功");
        result.put("data", token);

        return result;
    }
}
