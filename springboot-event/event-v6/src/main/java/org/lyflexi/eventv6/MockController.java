package org.lyflexi.eventv6;

import lombok.extern.slf4j.Slf4j;
import org.lyflexi.eventv6.event.CustomEvent;
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
        applicationContext.publishEvent(new CustomEvent("str"));
    }

}
