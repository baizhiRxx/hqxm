package com.baizhi.cache;

import com.baizhi.util.SerializeUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@Aspect
public class RedisCache {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Around("@annotation(com.baizhi.annotation.QueryAnnotation)")
    public Object addCache(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 设计 Key
        String classname = proceedingJoinPoint.getTarget().getClass().getName();
        // 设计 key
        String key = "";
        key += classname;
        String methodname = proceedingJoinPoint.getSignature().getName();
        key += methodname;
        Object[] args = proceedingJoinPoint.getArgs();
        for (Object arg : args) {
            key += arg;
        }
        // 判断缓存中是否存在该数据
        Boolean flag = stringRedisTemplate.opsForHash().hasKey(classname, key);
        if (flag){
            // 从缓存中取出数据
            String o = (String) stringRedisTemplate.opsForHash().get(classname, key);
            Object o1 = SerializeUtils.serializeToObject(o);
            return o1;
        }else {
            // 将数据存入缓存中
            Object proceed = proceedingJoinPoint.proceed();
            String serialize = SerializeUtils.serialize(proceed);
            stringRedisTemplate.opsForHash().put(classname,key,serialize);
            return proceed;
        }
    }
    @After("@annotation(com.baizhi.annotation.FlushAnnotation)")
    public void clear(JoinPoint joinPoint){
        String classname = joinPoint.getTarget().getClass().getName();
        stringRedisTemplate.delete(classname);
    }
}
