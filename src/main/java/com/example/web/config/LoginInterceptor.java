package com.example.web.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //通过session判断是否放行
        Object username  = request.getSession().getAttribute("loginName");
        if (username == null){
            request.setAttribute("msg","没有权限，请先登录");
            request.getRequestDispatcher("/login").forward(request,response);
            return false;
        }else
            return true;
    }
}
