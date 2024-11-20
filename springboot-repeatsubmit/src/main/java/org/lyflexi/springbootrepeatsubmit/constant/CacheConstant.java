package org.lyflexi.springbootrepeatsubmit.constant;

/**
 * @Description:
 * @Author: lyflexi
 * @project: springboot-practice
 * @Date: 2024/11/20 11:02
 */
public class CacheConstant {

    /**
     * 分布式锁缓存前缀
     */
    public static final String DLOCK_PREFIX = "DLOCK:redssion";
    /**
     * 分布式锁获取超时时间，单位：毫秒
     */
    public static final long DLOCK_TRY_LOCK_TIMEOUT = 10000L;

    /**
     * 添加货架接口的分布式锁方法名称
     */
    public static final String ADD_FORM = "ADD_FORM";}
