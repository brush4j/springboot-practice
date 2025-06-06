package org.lyflexi.debug_springboot.eventdriverprogrammer.subscriber;


import org.lyflexi.debug_springboot.eventdriverprogrammer.LoginSuccessEvent;
import org.lyflexi.debug_springboot.eventdriverprogrammer.UserEntity;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * @author lyflexi
 * @Description
 * @create 2023-04-24 18:43
 */
@Order(2)
@Service
public class AccountService implements ApplicationListener<LoginSuccessEvent> {




    public void addAccountScore(String username){
        System.out.println(username +" 加了1分");
    }

    @Override
    public void onApplicationEvent(LoginSuccessEvent event) {
        System.out.println("=====  AccountService  收到事件 =====");

        UserEntity source = (UserEntity) event.getSource();
        addAccountScore(source.getUsername());
    }
}
