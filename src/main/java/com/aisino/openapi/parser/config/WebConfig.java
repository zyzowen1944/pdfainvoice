package com.aisino.openapi.parser.config;

import com.aisino.openapi.parser.web.interconnected.ApiAuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Autowired
    private ApiAuthInterceptor apiAuthInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiAuthInterceptor)
                .addPathPatterns("/api/v1/deepseek/**") // 需要拦截的路径
                .excludePathPatterns("/api/v1/invoice/**"); // 排除的路径
    }
}
