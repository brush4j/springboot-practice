package com.lyflexi.springboottx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyflexi.springboottx.dao.UserMapper;
import com.lyflexi.springboottx.model.param.UserParam;
import com.lyflexi.springboottx.model.po.UserPo;
import com.lyflexi.springboottx.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPo> implements IUserService {

    @Autowired
    private IUserService userService; // 注入自己，用于演示不同事务

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void process(UserParam param) {
        UserPo userPo = new UserPo();
        BeanUtils.copyProperties(param, userPo);
        this.save(userPo);
        log.info("用户新增成功，ID=[{}]", userPo.getId());

        // 同一事务内查询
        UserPo againUser = this.getById(userPo.getId());
        log.info("同一事务内查询结果：[{}]", againUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class/*, propagation = Propagation.REQUIRES_NEW*/)
    public UserPo queryById(Long id) {
        UserPo user = this.getById(id);
        log.info("新事务中查询结果：[{}]", user);
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void testTransactionIsolation(UserParam param) {
        // 1. 先插入数据
        UserPo userPo = new UserPo();
        BeanUtils.copyProperties(param, userPo);
        this.save(userPo);
        log.info("事务1：用户新增成功，ID=[{}]", userPo.getId());

        // 2. 在新事务中查询数据
        UserPo result = userService.queryById(userPo.getId());
        log.info("事务2：新事务查询结果=[{}]", result);

        // 3. 当前事务中查询数据
        UserPo currentTxResult = this.getById(userPo.getId());
        log.info("事务1：当前事务查询结果=[{}]", currentTxResult);

        // 4. 模拟长时间操作，便于观察
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}