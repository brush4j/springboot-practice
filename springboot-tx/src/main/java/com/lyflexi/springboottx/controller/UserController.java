package com.lyflexi.springboottx.controller;

import com.lyflexi.springboottx.model.param.UserParam;
import com.lyflexi.springboottx.model.po.UserPo;
import com.lyflexi.springboottx.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    IUserService userService;

    /**
     * @description: 同一事务内查询
     * @author: hmly
     * @date: 2025/7/25 0:00
     * @param: [param]
     * @return: void
     **/
    @PostMapping(value = "/add")
    public void add (@RequestBody UserParam param) {
        userService.process(param);
    }

    /**
     * @description: 不同事务内查询（默认RR可重复读隔离级别）
     * @author: hmly
     * @date: 2025/7/25 0:00
     * @param: [param]
     * @return: void
     **/
    @PostMapping(value = "/add2")
    public void add2 (@RequestBody UserParam param) {
        userService.testTransactionIsolation(param);
    }
}
