package org.lyflexi.eventv3.entity;

import lombok.Data;

/**
 * @Description:
 * @Author: lyflexi
 * @project: springboot-practice
 * @Date: 2024/10/31 10:54
 */
@Data
public class Person {
    private String name;

    public Person(String name) {
        this.name = name;
    }
}