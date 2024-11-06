package com.lyflexi.springbootpure.mock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * 测试git-curl请求中，post请求体字段，带中文值
     */
    @PostMapping(value = "/git-curl-post")
    public void gitChinesePost (@RequestBody MockData data) {
        log.info(data.toString());
    }
}
