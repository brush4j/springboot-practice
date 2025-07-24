package com.lyflexi.springboottx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lyflexi.springboottx.dao")
public class SpringbootTxApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootTxApplication.class, args);
    }

}
