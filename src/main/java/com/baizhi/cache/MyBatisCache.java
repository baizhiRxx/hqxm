package com.baizhi.cache;

import com.baizhi.util.SerializeUtils;
import com.baizhi.util.SpringContextUtil;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class MyBatisCache implements Cache {

    private final String id;

    // 必须存在构造方法，且必须带有一个String类型的构造方法
    public MyBatisCache(String id) {
        this.id = id;
    }

    @Override
    // 缓存要求返回当前的namespace
    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(Object key, Object value) {
        StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) SpringContextUtil.getBean(StringRedisTemplate.class);
        String serialize = SerializeUtils.serialize(value);
        stringRedisTemplate.opsForHash().put(this.id,key.toString(),serialize);
    }

    @Override
    public Object getObject(Object key) {
        StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) SpringContextUtil.getBean(StringRedisTemplate.class);
        Boolean aBoolean = stringRedisTemplate.opsForHash().hasKey(this.id, key.toString());
        if (aBoolean){
            String o = (String) stringRedisTemplate.opsForHash().get(this.id, key.toString());
            Object o1 = SerializeUtils.serializeToObject(o);
            return o1;
        }
        return null;
    }

    @Override
    public Object removeObject(Object key) {
        System.out.println(key);
        return null;
    }

    @Override
    public void clear() {
        StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) SpringContextUtil.getBean(StringRedisTemplate.class);
        stringRedisTemplate.delete(this.id);
    }

    @Override
    public int getSize() {
        StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) SpringContextUtil.getBean(StringRedisTemplate.class);
        Long size = stringRedisTemplate.opsForHash().size(this.id);
        return size.intValue();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return new ReentrantReadWriteLock();
    }
}
