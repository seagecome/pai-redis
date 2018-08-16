package com.pai.base.redis;

import com.pai.base.utils.ObjectUtil;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.util.SafeEncoder;

public class SimpleRedisManager implements RedisManager {

    private JedisPool jedisPool = null;

    private int expire = 0;

    @Override
    public long decr(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.decr(key);
        } finally {
            jedis.close();
        }
    }

    @Override
    public void del(byte[] key) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.del(key);
        } finally {
            jedis.close();
        }
    }

    @Override
    public void del(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.del(key);
        } finally {
            jedis.close();
        }
    }

    @Override
    public long expire(String key, int seconds) {
        if (key == null || key.equals("")) {
            return 0;
        }
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.expire(key, seconds);
        } finally {
            jedis.close();
        }
    }

    @Override
    public long expireAt(String key, int unixTimestamp) {
        if (key == null || key.equals("")) {
            return 0;
        }
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.expireAt(key, unixTimestamp);
        } finally {
            jedis.close();
        }
    }

    @Override
    public byte[] get(byte[] key) {
        byte[] value = null;
        Jedis jedis = jedisPool.getResource();
        try {
            value = jedis.get(key);
        } finally {
            jedis.close();
        }
        return value;
    }

    @Override
    public String get(String key) {
        String value = null;
        Jedis jedis = jedisPool.getResource();
        try {
            value = jedis.get(key);
        } finally {
            jedis.close();
        }
        return value;
    }

    @Override
    public String get(String key, String defaultValue) {
        Jedis jedis = jedisPool.getResource();
        try {
            String value = jedis.get(key);
            return value == null ? defaultValue : value;
        } finally {
            jedis.close();
        }
    }

    @Override
    public Long incr(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.incr(key);
        } finally {
            jedis.close();
        }
    }

    @Override
    public byte[] set(byte[] key, byte[] value) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(key, value);
            if (this.expire != 0) {
                jedis.expire(key, this.expire);
            }
        } finally {
            jedis.close();
        }
        return value;
    }

    @Override
    public String set(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(key, value);
        } finally {
            jedis.close();
        }
        return value;
    }

    @Override
    public byte[] set(byte[] key, byte[] value, int expire) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(key, value);
            if (expire != 0) {
                jedis.expire(key, expire);
            }
        } finally {
            jedis.close();
        }
        return value;
    }

    @Override
    public String set(String key, String value, int second) {
        Jedis jedis = jedisPool.getResource();
        ;
        try {
            jedis.setex(key, second, value);
        } finally {
            jedis.close();
        }
        return value;
    }

    @Override
    public long setnx(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.setnx(key, value);
        } finally {
            jedis.close();
        }
    }

    /**
     * @param key   字符串的缓存key
     * @param value 缓存数据
     * @return 返回
     * @Description: 添加一个缓存数据
     */
    @Override
    public String add(String key, Object value) {
        Jedis jedis = jedisPool.getResource();
        jedis.set(SafeEncoder.encode(key), ObjectUtil.objectToBytes(value));
        if (this.expire != 0) {
            jedis.expire(key, this.expire);
        }
        return key;
    }

    /**
     * @param key     缓存key
     * @param value   缓存数据
     * @param seconds 缓存时长
     * @return 返回
     * @Description: 缓存一个数据，并指定缓存过期时间
     */
    @Override
    public String add(String key, Object value, int seconds) {
        Jedis jedis = jedisPool.getResource();
        jedis.set(SafeEncoder.encode(key), ObjectUtil.objectToBytes(value));
        if (seconds != 0) {
            jedis.expire(key, seconds);
        }else if (this.expire != 0){
            jedis.expire(key, this.expire);
        }
        return key;
    }

    /**
     * @param key 字符串的缓存key
     * @return 返回
     * @Description: 根据key读取缓存值
     */
    @Override
    public Object read(String key) {
        Jedis jedis = jedisPool.getResource();
        if(StringUtils.isBlank(key)){
            return null;
        }
        byte[] caches = jedis.get(SafeEncoder.encode(key));
        if(null != caches){
            return ObjectUtil.bytesToObject(caches);
        }
        return null;
    }

    /**
     * @param key 字符串的缓存key
     * @return 返回
     * @Description: 删除缓存数据
     */
    @Override
    public long delete(String key) {
        Jedis jedis = jedisPool.getResource();
        return jedis.del(key);
    }

    /**
     * @param key 字符串的缓存key
     * @return 返回
     * @Description: 判断指定key是否在缓存中存在
     */
    @Override
    public boolean exists(String key) {
        Jedis jedis = jedisPool.getResource();
        return jedis.exists(key);
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }
}
