package org.lyflexi.springbootrepeatsubmit.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.lyflexi.springbootrepeatsubmit.annotation.DistributedLock;
import org.lyflexi.springbootrepeatsubmit.constant.CacheConstant;
import org.lyflexi.springbootrepeatsubmit.entity.form.UserForm;
import org.lyflexi.springbootrepeatsubmit.service.MockService;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: lyflexi
 * @project: springboot-practice
 * @Date: 2024/11/20 11:10
 */
@Service
@Slf4j
public class MockServiceImpl implements MockService {
    @Override
    @DistributedLock(method = CacheConstant.ADD_FORM, uniqueKey = "#addForm.name")
    public void addForm(UserForm form) {
        log.info("current form:{}",form);
    }
}
