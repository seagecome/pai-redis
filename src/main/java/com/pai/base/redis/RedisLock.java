package com.pai.base.redis;

import com.pai.base.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * redis 锁
 */
public class RedisLock implements Lock {

    /**
     * 日志器
     */
    private static final Logger log = LoggerFactory.getLogger(RedisLock.class);
    /**
     * 默认Try超时时间
     */
    public static int DEFAULT_TIME_OUT = 6000;
    /**
     * Redis管理器
     */
    private RedisManager redisManager;
    /**
     * 锁定KEY值
     */
    private String lockKey;
    /**
     * 默认KEY过期时间,单位:秒
     */
    private int expire = 10;
    /**
     * 加锁状态
     */
    private boolean isLocked = false;

    public RedisLock() {
    }

    public RedisLock(RedisManager redisManager, String lockKey) {
        this.redisManager = redisManager;
        this.lockKey = lockKey;
    }

    @Override
    public void lock() {
        try {
            tryLock(DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("尝试锁定KEY:{}失败", lockKey);
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        try {
            return tryLock(0L, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("尝试锁定KEY:{}失败", lockKey);
            log.error(e.getMessage(), e);
        }
        return false;
    }

    public boolean tryLockNotExpire(String lockKey) {
        try {
            this.lockKey = lockKey;
            this.expire = -1;
            return tryLockWithTime(0L, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("尝试锁定KEY:{}失败", lockKey);
            log.error(e.getMessage(), e);
        }
        return false;
    }

    public boolean tryLockNotExpire(String lockKey, long timeout) {
        try {
            this.lockKey = lockKey;
            this.expire = -1;
            return tryLockWithTime(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("尝试锁定KEY:{}失败", lockKey);
            log.error(e.getMessage(), e);
        }
        return false;
    }

    public void unlock(String lockKey) {
        this.lockKey = lockKey;
        if (!isLocked)
            return;
        log.debug("尝试解锁KEY:{}", lockKey);
        Jedis jedis = null;
        try {
            redisManager.del(lockKey);
            isLocked = false;
        } catch (Exception e) {
            log.error("解锁KEY值{}失败", lockKey);
            log.error(e.getMessage(), e);
        }
    }

    public boolean tryLock(long timeout) {
        try {
            return tryLock(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("尝试锁定KEY:{}失败", lockKey);
            log.error(e.getMessage(), e);
        }
        return false;
    }

    public boolean tryLockWithTime(long timeout, TimeUnit unit) throws InterruptedException {
        log.debug("尝试锁定KEY:{}", lockKey);
        try {
            long nano = System.nanoTime();
            do {
                Long i = redisManager.setnx(lockKey, DateUtils.getCurrentFullDateStr());

                if (i == 1) {
                    isLocked = true;
                    redisManager.expire(lockKey, this.expire);
                    log.debug("成功将{}锁定{}秒", lockKey, expire);
                    return Boolean.TRUE;
                } else { // 存在锁
                    if (timeout == 0) {
                        log.debug("KEY值{}已经被锁定", lockKey);
                        break;
                    } else {
                        log.debug("KEY值{}已经被锁定,等待", lockKey);
                    }
                }
                Thread.sleep(300);
            } while ((System.nanoTime() - nano) < unit.toNanos(timeout));
            return Boolean.FALSE;
        } catch (Exception e) {
            log.error("锁定KEY值{}失败", lockKey);
            log.error(e.getMessage(), e);
        }
        return Boolean.FALSE;
    }

    @Override
    public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
        log.debug("尝试锁定KEY:{}", lockKey);
        try {
            long nano = System.nanoTime();
            do {
                Long i = redisManager.setnx(lockKey, lockKey);

                if (i == 1) {
                    isLocked = true;
                    redisManager.expire(lockKey, this.expire);
                    log.debug("成功将{}锁定{}秒", lockKey, expire);
                    return Boolean.TRUE;
                } else { // 存在锁
                    if (timeout == 0) {
                        log.debug("KEY值{}已经被锁定", lockKey);
                        break;
                    } else {
                        log.debug("KEY值{}已经被锁定,等待", lockKey);
                    }
                }
                Thread.sleep(300);
            } while ((System.nanoTime() - nano) < unit.toNanos(timeout));
            return Boolean.FALSE;
        } catch (Exception e) {
            log.error("锁定KEY值{}失败", lockKey);
            log.error(e.getMessage(), e);
        }
        return Boolean.FALSE;
    }

    @Override
    public void unlock() {
        if (!isLocked)
            return;
        log.debug("尝试解锁KEY:{}", lockKey);
        try {
            redisManager.del(lockKey);
            isLocked = false;
        } catch (Exception e) {
            log.error("解锁KEY值{}失败", lockKey);
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public String getLockKey() {
        return lockKey;
    }

    public int getExpire() {
        return expire;
    }

    public RedisManager getRedisManager() {
        return redisManager;
    }

    public void setRedisManager(RedisManager redisManager) {
        this.redisManager = redisManager;
    }
}