package com.lyflexi.springbootImportBeanDefinitionRegistrar;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： hmly
 * @date： 2025/6/2
 * @description：
 * @modifiedBy：
 * @version: 1.0
 */
public class GreetingServiceRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 动态注册 EnglishGreetingService
        GenericBeanDefinition englishGreetingServiceDefinition = new GenericBeanDefinition();
        englishGreetingServiceDefinition.setBeanClassName(EnglishGreetingService.class.getName());
        registry.registerBeanDefinition("englishGreetingService", englishGreetingServiceDefinition);

        // 动态注册 SpanishGreetingService
        GenericBeanDefinition spanishGreetingServiceDefinition = new GenericBeanDefinition();
        spanishGreetingServiceDefinition.setBeanClassName(SpanishGreetingService.class.getName());
        registry.registerBeanDefinition("spanishGreetingService", spanishGreetingServiceDefinition);
    }
}
