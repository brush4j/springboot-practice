package org.lyflexi.eventv2.listener;

import lombok.extern.slf4j.Slf4j;
import org.lyflexi.eventv1.entity.Order;
import org.lyflexi.eventv1.entity.Person;
import org.lyflexi.eventv2.event.BaseEvent;
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
    public void handleEvent(BaseEvent<?> baseEvent) {
        Object data = baseEvent.getData();
        if (data instanceof Person) {
            log.info("监听到PersonEvent: {}", baseEvent);
        } else if (data instanceof Order) {
            log.info("监听到OrderEvent: {}", baseEvent);
        }

    }
}


