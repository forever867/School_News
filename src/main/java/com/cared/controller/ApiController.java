package com.cared.controller;

import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowCredentials = "true")  // 允许 UniApp 跨域
public class ApiController {

    // ==================== 统一登录接口 ====================
    @PostMapping("/api/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginData) {

        String loginMode = loginData.get("loginMode");        // guest / student
        String schoolName = loginData.get("schoolName");
        String studentId = loginData.get("studentId");
        String password = loginData.get("password");

        Map<String, Object> result = new HashMap<>();

        if ("guest".equals(loginMode)) {
            // 游客登录
            result.put("code", 200);
            result.put("message", "游客登录成功");
            result.put("data", Map.of(
                    "type", "guest",
                    "nickname", "游客用户",
                    "token", "guest_token_" + System.currentTimeMillis()
            ));
        }
        else if ("student".equals(loginMode)) {
            // 学生登录
            if (studentId == null || password == null || schoolName == null) {
                result.put("code", 400);
                result.put("message", "学号、密码和学校不能为空");
                return result;
            }

            // 这里可后续接入各学校教务系统API（目前模拟验证）
            boolean loginSuccess = "admin".equals(studentId) && "123456".equals(password)
                    || studentId.length() >= 6;   // 简单模拟

            if (loginSuccess) {
                result.put("code", 200);
                result.put("message", "登录成功");
                result.put("data", Map.of(
                        "type", "student",
                        "school", schoolName,
                        "studentId", studentId,
                        "token", "stu_token_" + System.currentTimeMillis(),
                        "name", "学生用户"
                ));
            } else {
                result.put("code", 401);
                result.put("message", "学号或密码错误");
            }
        }
        else {
            result.put("code", 400);
            result.put("message", "登录模式错误");
        }

        return result;
    }
}