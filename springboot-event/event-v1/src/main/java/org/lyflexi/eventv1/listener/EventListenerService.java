package org.lyflexi.eventv1.listener;

import lombok.extern.slf4j.Slf4j;
import org.lyflexi.eventv1.event.OrderEvent;
import org.lyflexi.eventv1.event.PersonEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: lyflexi
 * @project: springboot-practice
 * @Date: 2024/10/31 10:56
 */
@Slf4j
@Component
public class EventListenerService {

    @EventListener
    public void handlePersonEvent(PersonEvent personEvent) {
        log.info("监听到PersonEvent: {}", personEvent);
    }
    @EventListener
    public void handleOrderEvent(OrderEvent orderEvent) {
        log.info("监听到orderEvent: {}", orderEvent);
    }

}
