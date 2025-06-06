package org.lyflexi.secondlevel_robotstarter.service;


import org.lyflexi.secondlevel_robotstarter.properties.RobotProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lyflexi
 * @Description
 * @create 2023-04-27 19:58
 */
@Service
public class RobotService {

    @Autowired
    RobotProperties robotProperties;

    public String sayHello(){
        return "你好：名字：【"+robotProperties.getName()+"】;年龄：【"+robotProperties.getAge()+"】";
    }
}
