package org.lyflexi.eventv5.event;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: lyflexi
 * @project: springboot-practice
 * @Date: 2024/10/31 13:47
 */
@Data
@NoArgsConstructor
public class BaseEvent<T>  {
    private T data;
    private String addOrUpdate;

    public BaseEvent(T data, String addOrUpdate) {
        this.data = data;
        this.addOrUpdate = addOrUpdate;
    }
    
}