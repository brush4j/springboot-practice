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
     * @description: 如果Read View视图的creator_trx_id，在undolog快照链中存在
     *
     * 那么数据记录的最后一次更新操作的事务就是当前事务，该版本的记录对当前事务可见
     * @author: hmly
     * @date: 2025/7/25 21:38
     * @param: []
     * @return: void
     **/
    @Test
    @Transactional
    public void testRRSnapshootRead() {
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


}
