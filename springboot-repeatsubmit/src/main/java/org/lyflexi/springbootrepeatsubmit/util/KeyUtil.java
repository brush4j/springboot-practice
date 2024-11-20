package org.lyflexi.springbootrepeatsubmit.util;

/**
 * @Description:
 * @Author: lyflexi
 * @project: springboot-practice
 * @Date: 2024/11/20 11:07
 */
public class KeyUtil {

    /**
     * 生成缓存key
     * @param cacheName 缓存名称
     * @param args 缓存描述，包含业务信息
     * @return cacheKey
     */
    public static String buildKey (String cacheName, String ... args) {
        return RedisKey.generator(cacheName, args);
    }
}
