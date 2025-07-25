package com.lyflexi.springboottx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyflexi.springboottx.dao.UserMapper;
import com.lyflexi.springboottx.model.param.UserParam;
import com.lyflexi.springboottx.model.po.UserPo;
import com.lyflexi.springboottx.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPo> implements IUserService {

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public UserPo getByIdOfRequiredNew(Long id) {
        UserPo user = this.getById(id);
        log.info("T2事务中查询结果：[{}]", user);
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public Boolean updateByIdOfRequiredNew(UserPo userPo) {
        return this.updateById(userPo);
    }

}