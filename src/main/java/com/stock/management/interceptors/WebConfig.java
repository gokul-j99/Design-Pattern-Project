package com.stock.management.interceptors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private UserValidationInterceptor userValidationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Apply the interceptor to all routes under /api/transactions
        registry.addInterceptor(userValidationInterceptor)
                .addPathPatterns("/api/transactions/**","/api/stocks/**");
    }
}
