package com.xavi.exams.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/api/student/stud-loginform").setViewName("login");
//        registry.addViewController("/api/instructor/lec-loginform").setViewName("login");
    }
}
