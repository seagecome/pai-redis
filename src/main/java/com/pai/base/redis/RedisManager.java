package com.pai.base.redis;

public interface RedisManager {

    long decr(String key);

    void del(byte[] key);

    void del(String key);

    long expire(String key, int seconds);

    long expireAt(String key, int unixTimestamp);

    byte[] get(byte[] key);

    String get(String key);

    String get(String key, String defaultValue);

    Long incr(String key);

    byte[] set(byte[] key, byte[] value);

    String set(String key, String value);

    byte[] set(byte[] key, byte[] value, int expire);

    String set(String key, String value, int second);

    long setnx(String key, String value);

    /**
     * @Description: 添加一个缓存数据
     * @param key   字符串的缓存key
     * @param value 缓存数据
     * @return 返回
     */
    String add(String key, Object value);

    /**
     * @Description: 缓存一个数据，并指定缓存过期时间
     * @param key 缓存key
     * @param value 缓存数据
     * @param seconds 缓存时长
     * @return 返回
     */
    String add(String key, Object value, int seconds);

    /**
     * @Description: 根据key读取缓存值
     * @param key 字符串的缓存key
     * @return 返回
     */
    <T>T read(String key);

    /**
     * @Description: 删除缓存数据
     * @param key 字符串的缓存key
     * @return 返回
     */
    long delete(String key);

    /**
     * @Description: 判断指定key是否在缓存中存在
     * @param key 字符串的缓存key
     * @return 返回
     */
    boolean exists(String key);
}
