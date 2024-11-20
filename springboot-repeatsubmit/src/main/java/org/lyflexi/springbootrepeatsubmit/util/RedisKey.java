package org.lyflexi.springbootrepeatsubmit.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @Description:
 * @Author: lyflexi
 * @project: springboot-practice
 * @Date: 2024/11/20 11:08
 */
public class RedisKey {
    private static final String KEY_CONCAT_CHAR = ":";

    public static String generator(String model,String... keys){
        StringBuffer sb = new StringBuffer();
        sb.append(model);
        sb.append(KEY_CONCAT_CHAR);
        sb.append(StringUtils.join(keys,KEY_CONCAT_CHAR));
        return sb.toString();
    }
}
