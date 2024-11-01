package org.lyflexi.eventv5.event;

import lombok.Data;
import org.lyflexi.eventv5.entity.Order;

/**
 * @Description:
 * @Author: lyflexi
 * @project: springboot-practice
 * @Date: 2024/11/1 9:06
 */
@Data
public class OrderEvent extends BaseEvent<Order>{
    public OrderEvent(Order order, String addOrUpdate){
        super(order,addOrUpdate);
    }
}
