package com.pai.base.redis;

import com.pai.base.utils.ObjectUtil;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.JedisCluster;
import redis.clients.util.SafeEncoder;

public class ClusterRedisManager implements RedisManager {

    private JedisCluster jedisCluster;

    private int expire = 0;

    @Override
    public long decr(String key) {
        return jedisCluster.decr(key);
    }

    @Override
    public void del(byte[] key) {
        jedisCluster.del(key);
    }

    @Override
    public void del(String key) {
        jedisCluster.del(key);
    }

    @Override
    public long expire(String key, int seconds) {
        return jedisCluster.expire(key, seconds);
    }

    @Override
    public long expireAt(String key, int unixTimestamp) {
        return jedisCluster.expireAt(key, unixTimestamp);
    }

    @Override
    public byte[] get(byte[] key) {
        return jedisCluster.get(key);
    }

    @Override
    public String get(String key) {
        return jedisCluster.get(key);
    }

    @Override
    public String get(String key, String defaultValue) {
        String value = jedisCluster.get(key);
        return value == null ? defaultValue : value;
    }

    @Override
    public Long incr(String key) {
        return jedisCluster.incr(key);
    }

    @Override
    public byte[] set(byte[] key, byte[] value) {
        jedisCluster.set(key, value);
        if (this.expire != 0) {
            jedisCluster.expire(key, this.expire);
        }
        return value;
    }

    @Override
    public String set(String key, String value) {
        jedisCluster.set(key, value);
        if (this.expire != 0) {
            jedisCluster.expire(key, this.expire);
        }
        return value;
    }

    @Override
    public byte[] set(byte[] key, byte[] value, int expire) {
        jedisCluster.set(key, value);
        if (expire != 0) {
            jedisCluster.expire(key, expire);
        }
        return value;
    }

    @Override
    public String set(String key, String value, int second) {
        jedisCluster.set(key, value);
        if (second != 0) {
            jedisCluster.expire(key, second);
        }
        return value;
    }

    @Override
    public long setnx(String key, String value) {
        return jedisCluster.setnx(key, value);
    }

    /**
     * @param key   字符串的缓存key
     * @param value 缓存数据
     * @return 返回
     * @Description: 添加一个缓存数据
     */
    @Override
    public String add(String key, Object value) {
        jedisCluster.set(SafeEncoder.encode(key), ObjectUtil.objectToBytes(value));
        if (this.expire != 0) {
            jedisCluster.expire(key, this.expire);
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
        jedisCluster.set(SafeEncoder.encode(key), ObjectUtil.objectToBytes(value));
        if (seconds != 0) {
            jedisCluster.expire(key, seconds);
        }else if (this.expire != 0){
            jedisCluster.expire(key, this.expire);
        }
        return key;
    }

    /**
     * @param key 字符串的缓存key
     * @return 返回
     * @Description: 根据key读取缓存值
     */
    @Override
    public <T>T read(String key) {
        if(StringUtils.isBlank(key)){
            return null;
        }
        byte[] caches = jedisCluster.get(SafeEncoder.encode(key));
        if(null != caches){
            return (T) ObjectUtil.bytesToObject(caches);
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
        return jedisCluster.del(key);
    }

    /**
     * @param key 字符串的缓存key
     * @return 返回
     * @Description: 判断指定key是否在缓存中存在
     */
    @Override
    public boolean exists(String key) {
        return jedisCluster.exists(key);
    }

    public JedisCluster getJedisCluster() {
        return jedisCluster;
    }

    public void setJedisCluster(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }
}
