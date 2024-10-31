package org.lyflexi.eventv1;

import lombok.extern.slf4j.Slf4j;
import org.lyflexi.eventv1.entity.Order;
import org.lyflexi.eventv1.entity.Person;
import org.lyflexi.eventv1.event.OrderEvent;
import org.lyflexi.eventv1.event.PersonEvent;
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
        applicationContext.publishEvent(new OrderEvent(new Order("order"),"add"));
        applicationContext.publishEvent(new PersonEvent(new Person("person"),"add"));
    }
}
