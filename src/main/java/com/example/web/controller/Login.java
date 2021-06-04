package com.example.web.controller;

import com.example.web.MyUtil.MD5;
import com.example.web.MyUtil.Record;
import com.example.web.pojo.Seller;
import com.example.web.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class Login {
    @Autowired
    JdbcTemplate jdbcTemplate;

    //判断用户是否能够登录
    @GetMapping("/login_do")
    public String login_do(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam(value = "check", required = false) Boolean check,
                           Model model, HttpSession session, HttpServletRequest request) {
        //判断用户名和密码
        if (!username.isEmpty() && !password.isEmpty()) {
            if (check != null) {    //判断是否是后台登录
                //root登录
                if(username.equals("root@root.com") && password.equals("123456")){
                    session.setAttribute("rootName",username);
                    Record.loginRecord(username,false,request,jdbcTemplate);
                    return "redirect:/rootManage";
                }
                //卖家登录
                String sql = "SELECT * FROM seller WHERE name =  ? AND password = ?";
                List<Seller> lists = jdbcTemplate.query(sql, new Object[]{username, MD5.encrypt(password)}, new BeanPropertyRowMapper<>(Seller.class));
                if (lists.size() == 1) {   //成功找到对应的卖家
                    session.setAttribute("sellerName", username);
                    session.setAttribute("sellerId", lists.get(0).getSellerId());
                    session.setAttribute("brandName", lists.get(0).getBrandName());
                    Record.loginRecord(username,false,request,jdbcTemplate);
                    return "redirect:/sellerManage";
                }
            } else {    //不是后台登录即商城用户登录
                //商城用户登录查询是否有对应用户
                String sql = "SELECT * FROM user WHERE name =  ? AND password = ?";
                List<User> lists = jdbcTemplate.query(sql, new Object[]{username, MD5.encrypt(password)}, new BeanPropertyRowMapper<>(User.class));
                if (lists.size() == 1) {   //成功找到对应用户
                    User loginUser = lists.get(0);
                    session.setAttribute("loginName", username);
                    session.setAttribute("loginUserId", loginUser.getUserId());
                    session.setAttribute("loginAddress", loginUser.getAddress());
                    session.setAttribute("loginEmail", loginUser.getEmail());
                    Record.loginRecord(username,false,request,jdbcTemplate);
                    return "redirect:/index";
                } else {    //商城用户登录失败返回错误消息
                    model.addAttribute("msg", "没有权限，请勾选后台登录后登录");
                    return "forward:/login";
                }
            }
        }
        model.addAttribute("msg", "用户名或密码错误");
        return "redirect:/login";
    }

    //注销用户数据
    @GetMapping("/logout")
    public String logout(HttpSession session,HttpServletRequest request) {
        if (session.getAttribute("loginName") != null) {
            Record.loginRecord(session.getAttribute("loginName").toString(),true,request,jdbcTemplate);
            session.removeAttribute("loginName");
            session.removeAttribute("loginUserId");
            session.removeAttribute("loginAddress");
            session.removeAttribute("loginEmail");
        }
        if (session.getAttribute("sellerName") != null) {
            Record.loginRecord(session.getAttribute("sellerName").toString(),true,request,jdbcTemplate);
            session.removeAttribute("sellerName");
            session.removeAttribute("sellerId");
            session.removeAttribute("brandName");
        }
        if (session.getAttribute("rootName") != null) {
            Record.loginRecord(session.getAttribute("rootName").toString(),true,request,jdbcTemplate);
            session.removeAttribute("rootName");
        }
        return "redirect:/login";
    }

    //用户注册
    @GetMapping("/register_do")
    public String register_do(@RequestParam(name = "email") String email,
                              @RequestParam(name = "password") String password,
                              @RequestParam(name = "passwordRepeat") String passwordRepeat,
                              @RequestParam(name = "address") String address,
                              Model model) {
        //电子邮件
        String check = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        boolean isMatched = matcher.matches();
        if (isMatched && password.equals(passwordRepeat)) {
            String sql = "INSERT INTO user(name, password, address, email) VALUES (?,?,?,?)";
            try {
                jdbcTemplate.update(sql, email, MD5.encrypt(password), address, email);
                return "redirect:/login_do?username=" + email + "&password=" + password;
            } catch (Exception e) {
                model.addAttribute("msg", "邮箱已被注册或两次密码不同");
                return "forward:/register";
            }
        }
        model.addAttribute("msg", "邮箱格式不符合要求");
        return "redirect:/register";
    }
}
