package com.example.web.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class SellerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //通过session判断是否放行
        Object sellerName = request.getSession().getAttribute("sellerName");
        Object rootName = request.getSession().getAttribute("rootName");
        if (sellerName == null && rootName == null) {
            request.setAttribute("msg", "没有权限，请勾选后台登录后登录");
            request.getRequestDispatcher("/login").forward(request, response);
            return false;
        } else
            return true;
    }
}
