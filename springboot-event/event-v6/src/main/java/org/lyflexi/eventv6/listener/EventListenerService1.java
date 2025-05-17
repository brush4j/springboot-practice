package org.lyflexi.eventv6.listener;

import lombok.extern.slf4j.Slf4j;
import org.lyflexi.eventv6.event.CustomEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: lyflexi
 * @project: springboot-practice
 * @Date: 2024/10/31 10:56
 */
@Slf4j
@Component
public class EventListenerService1 {

    @EventListener
    public void handleEvent(CustomEvent customerEvent) {
        log.info("EventListenerService1监听到MyCustomEvent: {}", customerEvent);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("EventListenerService1处理完MyCustomEvent: {}", customerEvent);
    }
}


