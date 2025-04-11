package com.lyflexi.springbootpure.config;

import com.lyflexi.springbootpure.resolver.ReqParamResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public ReqParamResolver reqParamResolver(){
        return new ReqParamResolver();
    }
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(reqParamResolver());
    }
}
