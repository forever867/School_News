package com.cared.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class LoginController {

    @RequestMapping("/")
    public String login(Model model, HttpServletRequest request) {
        model.addAttribute("prompt", "知识提示");

        // 获取表单参数
        String userPhone = request.getParameter("userPhone");
        String userPass = request.getParameter("userPass");
        String userVretical = request.getParameter("userVretical");

        // 这里可以加入你的登录逻辑
        if (userPhone != null && !userPhone.isEmpty()) {
            System.out.println("收到登录请求: " + userPhone);
            // model.addAttribute("prompt", "登录成功！");
        }

        return "login";   // 返回 templates/login.html
    }
}