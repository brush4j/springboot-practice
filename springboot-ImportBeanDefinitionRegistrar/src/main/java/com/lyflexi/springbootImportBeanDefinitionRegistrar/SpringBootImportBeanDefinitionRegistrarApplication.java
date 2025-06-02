package com.lyflexi.springbootImportBeanDefinitionRegistrar;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@Slf4j
public class SpringBootImportBeanDefinitionRegistrarApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootImportBeanDefinitionRegistrarApplication.class, args);


        // 获取并调用 EnglishGreetingService
        GreetingService englishService = context.getBean("englishGreetingService", GreetingService.class);
        log.info(englishService.sayGreeting()); // 输出: Hello!

        // 获取并调用 SpanishGreetingService
        GreetingService spanishService = context.getBean("spanishGreetingService", GreetingService.class);
        log.info(spanishService.sayGreeting()); // 输出: ¡Hola!

    }

}
