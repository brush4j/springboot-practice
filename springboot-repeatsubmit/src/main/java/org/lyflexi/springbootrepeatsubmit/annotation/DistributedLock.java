package org.lyflexi.springbootrepeatsubmit.annotation;

import org.lyflexi.springbootrepeatsubmit.constant.CacheConstant;

import java.lang.annotation.*;

/**
 * @Description:
 * @Author: lyflexi
 * @project: springboot-practice
 * @Date: 2024/11/20 11:02
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {
    /**
     * 获取Redis分布式锁的超时时间，毫秒，超时后获取失败
     */
    long timeout() default CacheConstant.DLOCK_TRY_LOCK_TIMEOUT;

    /**
     * 组成键的接口名称
     */
    String method();

    /**
     * 组成键的接口参数字段
     */
    String uniqueKey();
}
