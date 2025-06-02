package com.lyflexi.springbootImportBeanDefinitionRegistrar;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： hmly
 * @date： 2025/6/2
 * @description：
 * @modifiedBy：
 * @version: 1.0
 */
@Configuration
@Import(GreetingServiceRegistrar.class)
public class GlobalConfig {
    // 其他配置...
}