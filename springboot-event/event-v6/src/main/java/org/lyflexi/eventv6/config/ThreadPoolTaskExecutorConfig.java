package org.lyflexi.eventv6.config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： hmly
 * @date： 2025/5/17
 * @description：
 * @modifiedBy：
 * @version: 1.0
 */
@Configuration
@Slf4j
public class ThreadPoolTaskExecutorConfig {
    /**
     * @description: @Async请求的时候控制台警告2025-05-17T20:04:13.591+08:00  INFO 16744 --- [cutor1-thread-2] .s.a.AnnotationAsyncExecutionInterceptor : More than one TaskExecutor bean found within the context, and none is named 'taskExecutor'. Mark one of them as primary or name it 'taskExecutor' (possibly as an alias) in order to use it for async processing: [taskExecutor1, fakeTaskExecutor]
     * 线程池降级为Spring兜底的SimpleAsyncTaskExecutor线程池，而我们给事件多播器自定义的线程taskExecutor1没有生效
     * 解决办法就是
     * - 要么是将taskExecutor1重命名为taskExecutor
     * - 要么是将taskExecutor1指定为@Primary
     * @author: hmly
     * @date: 2025/5/17 21:18
     * @param: []
     * @return: org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
     **/
    @Bean
    public ThreadPoolTaskExecutor taskExecutor1() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor() {
            @Override
            public String getThreadNamePrefix() {
                return super.getThreadNamePrefix();
            }

            @Override
            public void afterPropertiesSet() {
                super.afterPropertiesSet();
                log.info("线程池初始化完成 - 核心线程数: {}, 最大线程数: {}, 队列容量: {}",
                        this.getCorePoolSize(),
                        this.getMaxPoolSize(),
                        this.getQueueCapacity());
            }
        };
        // 核心线程数
        executor.setCorePoolSize(5);
        // 最大线程数
        executor.setMaxPoolSize(10);
        // 队列容量
        executor.setQueueCapacity(25);
        // 线程名称前缀
        executor.setThreadNamePrefix("taskExecutor1-thread-");
        // 线程空闲时间
        executor.setKeepAliveSeconds(60);
        // 拒绝策略：当队列和线程池都满了之后的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }


    @Bean
    @Deprecated
    public ThreadPoolTaskExecutor fakeTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor() {
            @Override
            public String getThreadNamePrefix() {
                return super.getThreadNamePrefix();
            }

            @Override
            public void afterPropertiesSet() {
                super.afterPropertiesSet();
                log.info("线程池初始化完成 - 核心线程数: {}, 最大线程数: {}, 队列容量: {}",
                        this.getCorePoolSize(),
                        this.getMaxPoolSize(),
                        this.getQueueCapacity());
            }
        };
        // 核心线程数
        executor.setCorePoolSize(5);
        // 最大线程数
        executor.setMaxPoolSize(10);
        // 队列容量
        executor.setQueueCapacity(25);
        // 线程名称前缀
        executor.setThreadNamePrefix("fakeTaskExecutor-thread-");
        // 线程空闲时间
        executor.setKeepAliveSeconds(60);
        // 拒绝策略：当队列和线程池都满了之后的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }

    /**
     * @description: ApplicationEventPublisher会依赖于自定义的异步多播器ApplicationEventMulticaster
     * 且自定义的多播器ApplicationEventMulticaster名字必须为applicationEventMulticaster，否则Spring将自己创建
     * @author: hmly
     * @date: 2025/5/17 19:16
     * @param: [taskExecutor]
     * @return: org.springframework.context.event.SimpleApplicationEventMulticaster
     *
     **/
    @Bean("applicationEventMulticaster")
    public SimpleApplicationEventMulticaster simpleApplicationEventMulticaster(ThreadPoolTaskExecutor taskExecutor1) {
        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
        eventMulticaster.setTaskExecutor(taskExecutor1);
        return eventMulticaster;
    }


}