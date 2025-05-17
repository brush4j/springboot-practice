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
public class EventListenerService2 {

    @EventListener
    public void handleEvent(CustomEvent customerEvent) {
        log.info("EventListenerService2监听到MyCustomEvent: {}", customerEvent);
    }
}


