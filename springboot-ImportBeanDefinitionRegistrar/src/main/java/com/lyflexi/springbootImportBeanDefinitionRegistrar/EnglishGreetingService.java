package com.lyflexi.springbootImportBeanDefinitionRegistrar;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： hmly
 * @date： 2025/6/2
 * @description：
 * @modifiedBy：
 * @version: 1.0
 */
public class EnglishGreetingService implements GreetingService {
    @Override
    public String sayGreeting() {
        return "Hello!";
    }
}

