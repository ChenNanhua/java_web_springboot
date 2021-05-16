package com.example.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class Login {

    @RequestMapping("/login_do")
    public String login_do(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Model model, HttpSession session) {
        //判断用户名和密码
        if (!username.isEmpty() && !password.isEmpty()) {
            session.setAttribute("loginName", username);
            return "redirect:/index";
        } else {
            model.addAttribute("msg", "用户名或密码错误");
            return "forward:/login";
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        //注销用户
        if (session.getAttribute("loginName") != null) {
            session.removeAttribute("loginName");
        }
        return "redirect:/index";
    }
}
