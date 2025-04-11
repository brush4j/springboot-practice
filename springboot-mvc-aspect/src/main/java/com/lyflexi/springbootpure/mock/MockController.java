package com.lyflexi.springbootpure.mock;

import com.lyflexi.springbootpure.annotation.ReqParamAnno;
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
     * 测试根据用户请求头，映射对应的reqParamAnno值
     *
     * /mock/getByHeader
     * 请求头：USER-ID  1
     * 返回 param1
     */
    @PostMapping(value = "/getByHeader")
    public void gitChinesePost (@ReqParamAnno String reqParamAnno) {
        log.info(reqParamAnno.toString());
    }
}
