package org.lyflexi.eventv1.event;

import lombok.Data;
import org.lyflexi.eventv1.entity.Order;

/**
 * @Description:
 * @Author: lyflexi
 * @project: springboot-practice
 * @Date: 2024/10/31 10:48
 */
@Data
public class OrderEvent {

    private Order order;

    private String addOrUpdate;

    public OrderEvent(Order order, String addOrUpdate) {
        this.order = order;
        this.addOrUpdate = addOrUpdate;
    }
}