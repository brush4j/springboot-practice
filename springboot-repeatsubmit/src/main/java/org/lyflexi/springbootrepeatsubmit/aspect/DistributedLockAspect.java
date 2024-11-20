package org.lyflexi.springbootrepeatsubmit.aspect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.lyflexi.springbootrepeatsubmit.annotation.DistributedLock;
import org.lyflexi.springbootrepeatsubmit.constant.CacheConstant;
import org.lyflexi.springbootrepeatsubmit.util.KeyUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: lyflexi
 * @project: springboot-practice
 * @Date: 2024/11/20 11:04
 */

@Component
@Aspect
@Slf4j
public class DistributedLockAspect {
    private RedissonClient redissonClient;

    public DistributedLockAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        Object[] args = joinPoint.getArgs();
        ExpressionParser parser = new SpelExpressionParser();
        String[] nameArr = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < Objects.requireNonNull(nameArr).length; i++) {
            context.setVariable(nameArr[i], args[i]);
        }
        String lockKey = null;
        String uniqueKeyExpr = distributedLock.uniqueKey();
        if (uniqueKeyExpr.matches("^#.*.$")) {
            String uniqueKey = parser.parseExpression(uniqueKeyExpr).getValue(context, String.class);
            lockKey = KeyUtil.buildKey(CacheConstant.DLOCK_PREFIX, MessageFormat.format(":{0}:{1}:{2}",
                    uniqueKey, distributedLock.method()));
        }
        Object obj = null;
        if (StringUtils.isBlank(lockKey)) {
            log.error("分布锁spel表达式 {} 语法异常，请检查", uniqueKeyExpr);
            return obj;
        }
        RLock lock = redissonClient.getLock(lockKey);
        if (lock.tryLock(distributedLock.timeout(), TimeUnit.MILLISECONDS)) {
            log.info("分布式锁获取成功：key {}", lockKey);
            try {
                obj = joinPoint.proceed();
            } finally {
                if (lock != null && lock.isHeldByCurrentThread()) {
                    try {
                        log.info("分布式锁解锁成功：key {}", lockKey);
                        lock.unlock();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return obj;
    }
}
