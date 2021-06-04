package com.example.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    @Override   //所有基础页面的访问方式
    public void addViewControllers(ViewControllerRegistry registry) {
        //商城主界面
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/index.html").setViewName("index");
        //商城
        registry.addViewController("/shop").setViewName("/shop");
        registry.addViewController("/settle").setViewName("/settle");
        registry.addViewController("/orderList").setViewName("/orderList");
        //登录/注册
        registry.addViewController("/login").setViewName("/user/login");
        registry.addViewController("/register").setViewName("/user/register");
        //卖家
        registry.addViewController("/sellerManage").setViewName("/seller/manage");
        registry.addViewController("/sellerRecord").setViewName("/seller/record");
        //root
        registry.addViewController("/rootManage").setViewName("/root/manage");
        registry.addViewController("/rootRecord").setViewName("/root/record");
        registry.addViewController("/rootBigData").setViewName("/root/bigData");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/settle","/orderList").
                excludePathPatterns();
        registry.addInterceptor(new SellerInterceptor()).addPathPatterns("/rootManage","/sellerManage","/sellerRecord",
                "/rootRecord","/rootManage","/rootBigData").
                excludePathPatterns();
    }
}
