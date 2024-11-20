package org.lyflexi.springbootrepeatsubmit.controller;

import lombok.extern.slf4j.Slf4j;
import org.lyflexi.springbootrepeatsubmit.entity.form.UserForm;
import org.lyflexi.springbootrepeatsubmit.service.MockService;
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
@RequestMapping("/mock")
@Slf4j
public class MockController {

    @Autowired
    MockService mockService;
    /**
     * 重复提交
     */
    @PostMapping(value = "/repeatSubmit")
    public void gitChinesePost (@RequestBody UserForm form) {
        mockService.addForm(form);
    }
}
