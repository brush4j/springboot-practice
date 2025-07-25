package com.lyflexi.springboottx.rr;

import com.lyflexi.springboottx.model.po.UserPo;
import com.lyflexi.springboottx.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Slf4j
class RRTests {

    @Autowired
    private IUserService userService;
    /**
     * @description: 默认情况下自动提交模式，随心所欲的查询刚刚新增的记录
     * @author: hmly
     * @date: 2025/7/25 21:38
     * @param: []
     * @return: void
     **/
    @Test
    public void autoCommit() {
        UserPo userPo = new UserPo();
        userPo.setName("autoCommit");
        userPo.setAge(30);
        userService.save(userPo);
        log.info("add success...");
        UserPo byId1 = userService.getById(userPo.getId());
        log.info("byId1={}", byId1);
        UserPo byId2 = userService.getById(userPo.getId());
        log.info("byId2={}", byId2);
        userPo.setAge(40);
        userService.updateById(userPo);
        log.info("update success...");
        UserPo byId3 = userService.getById(userPo.getId());
        log.info("byId3={}", byId3);
    }

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void testRC() {
        UserPo userPo = new UserPo();
        userPo.setName("autoCommit");
        userPo.setAge(30);
        userService.save(userPo);
        log.info("add success...");
        // 在T2事务中查询数据
        UserPo result = userService.getByIdOfRequiredNew(userPo.getId());
        log.info("事务2：新事务查询结果=[{}]", result);
        // 当前事务中查询数据
        UserPo currentTxResult = userService.getById(userPo.getId());
        log.info("事务1：当前事务查询结果=[{}]", currentTxResult);
    }

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void testRR() {
        UserPo userPo = new UserPo();
        userPo.setName("autoCommit");
        userPo.setAge(30);
        userService.save(userPo);
        log.info("add success...{}",userPo);
        // 在T1事务自身可以更新数据
        userPo.setAge(40);
        userService.updateById(userPo);
        log.info("T1事务 update success...");
        // 在T2事务中更新数据被阻塞
        userPo.setAge(50);
        Boolean b = userService.updateByIdOfRequiredNew(userPo);//RR隔离级别下，T2事务被阻塞禁止更新，保证T1事务多次读取一致性
        log.info("事务T2：能否更新数据=[{}]", b);
    }

}
