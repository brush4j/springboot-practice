package org.lyflexi.eventv6.event;
import org.springframework.context.ApplicationEvent;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： hmly
 * @date： 2025/5/17
 * @description：
 * @modifiedBy：
 * @version: 1.0
 */
public class CustomEvent extends ApplicationEvent {
    public CustomEvent(Object source) {
        super(source);
    }
    // 可以添加额外的方法或字段
}