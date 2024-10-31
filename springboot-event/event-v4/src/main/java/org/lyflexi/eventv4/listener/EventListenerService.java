package org.lyflexi.eventv4.listener;

import lombok.extern.slf4j.Slf4j;
import org.lyflexi.eventv3.entity.Order;
import org.lyflexi.eventv3.entity.Person;
import org.lyflexi.eventv4.event.BaseEvent;
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
    public void handlePersonEvent(BaseEvent<Person> baseEvent) {
        log.info("监听到PersonEvent: {}", baseEvent);
    }

    @EventListener
    public void handleOrderEvent(BaseEvent<Order> baseEvent) {
        log.info("监听到OrderEvent: {}", baseEvent);
    }
}


