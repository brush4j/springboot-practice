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

    /***
     * 1、importingClassMetadata：提供有关正在导入的类的元数据信息，如类名、注解等。
     * 2、registry：用于注册 Bean 定义的 BeanDefinitionRegistry，可以使用这个注册中心来添加、移除或修改 Bean 定义。
     * @param importingClassMetadata annotation metadata of the importing class
     * @param registry current bean definition registry
     */
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
