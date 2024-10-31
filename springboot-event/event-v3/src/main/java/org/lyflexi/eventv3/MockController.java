package org.lyflexi.eventv3;

import lombok.extern.slf4j.Slf4j;
import org.lyflexi.eventv3.entity.Order;
import org.lyflexi.eventv3.entity.Person;
import org.lyflexi.eventv3.event.BaseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: lyflexi
 * @project: springboot-practice
 * @Date: 2024/10/31 10:51
 */
@RestController
@Slf4j
public class MockController {
    @Autowired
    ApplicationContext applicationContext;
    
    @GetMapping("/publishEvent")
    public void publishEvent(){
        applicationContext.publishEvent(new BaseEvent<>(new Order("order"),"add"));
        applicationContext.publishEvent(new BaseEvent<>(new Person("person"),"add"));
    }
}
