package org.lyflexi.eventv5.entity;

import lombok.Data;

/**
 * @Description:
 * @Author: lyflexi
 * @project: springboot-practice
 * @Date: 2024/10/31 10:48
 */
@Data
public class Order {
    private String orderName;

    public Order(String orderName) {
        this.orderName = orderName;
    }
}