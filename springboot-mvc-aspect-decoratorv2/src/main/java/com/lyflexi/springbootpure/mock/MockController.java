package com.lyflexi.springbootpure.mock;

import com.lyflexi.springbootpure.annotation.TimeStampRequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Description:
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/23 8:21
 */
@RestController
@RequestMapping("/mock")
@Slf4j
public class MockController {

    /**
     * 测试增强RequestBody注解
     *
     * /mock/enhanceRequestBody
     *
     * {
     *   "name": "ly"
     * }
     *
     * 在原有请求参数的基础上，塞进去时间戳
     */
    @PostMapping(value = "/enhanceRequestBody")
    public void enhanceRequestBody (@RequestBody Map<String,String> param) {
        log.info(param.toString());
    }
}
