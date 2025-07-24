package com.lyflexi.springboottx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyflexi.springboottx.model.param.UserParam;
import com.lyflexi.springboottx.model.po.UserPo;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： hmly
 * @date： 2025/7/24
 * @description：
 * @modifiedBy：
 * @version: 1.0
 */
public interface IUserService extends IService<UserPo> {
    void process(UserParam param);
    UserPo queryById(Long id);
    void testTransactionIsolation(UserParam param);
}
