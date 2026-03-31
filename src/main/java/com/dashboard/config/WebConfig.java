package com.dashboard.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.lang.NonNull;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AdminInterceptor adminInterceptor;

    @Override
    @SuppressWarnings("null")
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin/**");
    }

    @Override
    @SuppressWarnings("null")
    public void addResourceHandlers(@NonNull org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry registry) {
        // Expose the images directory so frontend can display them.
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:data/images/");
    }
}
