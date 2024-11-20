package org.lyflexi.springbootrepeatsubmit.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: ly
 * @Date: 2024/1/23 20:05
 */
@Configuration
public class RedissonConfig {

    @Autowired
    RedisProperties redisProperties;

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();

        config.useSingleServer()
                .setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort())
                .setPassword(redisProperties.getPassword())
                .setDatabase(redisProperties.getDatabase());
        return Redisson.create(config);

    }
}
