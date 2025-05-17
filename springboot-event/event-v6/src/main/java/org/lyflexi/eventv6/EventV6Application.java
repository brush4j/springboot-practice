package org.lyflexi.eventv6;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;


@SpringBootApplication
@Slf4j
public class EventV6Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(EventV6Application.class, args);

        ApplicationEventMulticaster multicaster = context.getBean(
                AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME,
                ApplicationEventMulticaster.class);

        if (multicaster instanceof SimpleApplicationEventMulticaster) {
            SimpleApplicationEventMulticaster simpleMulticaster = (SimpleApplicationEventMulticaster) multicaster;
            //因为`getTaskExecutor()`是protected方法，我们不能直接访问。我们可以通过反射方式访问
            ThreadPoolTaskExecutor executor = null;
            try {
                Method method = SimpleApplicationEventMulticaster.class.getDeclaredMethod("getTaskExecutor");
                method.setAccessible(true);
                executor  = (ThreadPoolTaskExecutor)method.invoke(multicaster);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            log.info("当前使用的线程池: {}, getThreadNamePrefix：{}", executor,executor.getThreadNamePrefix());
        }
    }


}
