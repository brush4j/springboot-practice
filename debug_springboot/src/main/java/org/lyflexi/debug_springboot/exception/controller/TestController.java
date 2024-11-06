package org.lyflexi.debug_springboot.exception.controller;

import lombok.extern.slf4j.Slf4j;
import org.lyflexi.debug_springboot.exception.LyFlexiBusinessException;
import org.lyflexi.debug_springboot.exception.LyflexiErrorType;
import org.springframework.web.bind.annotation.GetMapping;
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
public class TestController {

    @GetMapping(value = "/formatException")
    public void formatExceptionAdvice (){
        throw new LyFlexiBusinessException(LyflexiErrorType.NO_PULL_REPLENISH_CONFIG,new Object[]{"materialCode", "materialName","materialVersion"});
    }

    @GetMapping(value = "/formatExceptionAhead")
    public void formatException (){
        throw LyFlexiBusinessException.exception("需求[{0}]关联的工单排程基础信息丢失，无法继续执行！", "demandCode");
    }
}
