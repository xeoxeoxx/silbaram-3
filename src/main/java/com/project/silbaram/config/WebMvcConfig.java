package com.project.silbaram.config;

import com.project.silbaram.intercepter.AutoLoginInterceptor;
import com.project.silbaram.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private MemberService memberService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AutoLoginInterceptor(memberService))
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/logout");
    }
}