package com.pai.base.session;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.pai.base.redis.RedisManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.session.MapSession;

/**
 * redis管理操作MapSession
 */
@SuppressWarnings("unchecked")
public class RedisSessionManager {

    private static Logger logger = LoggerFactory.getLogger(RedisSessionManager.class);

    private RedisManager redisManager;

    private String prefix = "REDIS_SESSION:";

    /**
     * 过期时间默认60分钟
     */
    private int expire = 3600;

    // 创建session，保存到数据库
    public void doCreate(String sessionId, MapSession session) throws Exception {
        try {
            sessionId = prefix + sessionId;
            redisManager.set(sessionId.getBytes(), sessionToByte(session), expire);
        } catch (Exception e) {
            logger.error("创建redis session失败", e);
            throw e;
        }
    }

    // 获取session
    public MapSession doReadSession(String sessionId) throws Exception {
        if (StringUtils.isNotBlank(sessionId)) {
            try {
                sessionId = prefix + sessionId;
                byte[] bytes = redisManager.get(sessionId.getBytes());
                if (bytes != null && bytes.length > 0) {
                    return byteToSession(bytes);
                }
            } catch (Exception e) {
                logger.error("读取redis session失败", e);
                throw e;
            }
        }
        return null;
    }

    // 更新session的最后一次访问时间
    public void doUpdate(String sessionId, MapSession session) throws Exception {
        try {
            sessionId = prefix + sessionId;
            redisManager.set(sessionId.getBytes(), sessionToByte(session), expire);
        } catch (Exception e) {
            logger.error("更新redis session失败", e);
            throw e;
        }
    }

    // 删除session
    public void doDelete(String sessionId) throws Exception {
        try {
            sessionId = prefix + sessionId;
            redisManager.del(sessionId);
        } catch (Exception e) {
            logger.error("删除redis session失败", e);
            throw e;
        }
    }

    // 验证session
    public MapSession doVerify(String sessionId, String token) throws Exception {
        if (StringUtils.isNotBlank(sessionId) && StringUtils.isNotBlank(token)) {
            MapSession session = doReadSession(sessionId);
            if (session != null) {
                if (session.getId().equals(token)) {
                    redisManager.expire(sessionId, this.expire);
                    return session;
                }
            }
        }
        return null;
    }

    // 把session对象转化为byte保存到redis中
    protected byte[] sessionToByte(MapSession session) {
        byte[] bytes = null;
        try {
            MapSessionWrapper wrapper = new MapSessionWrapper(session);
            PropertyFilter filter = new PropertyFilter() {
                @Override
                public boolean apply(Object object, String name, Object value) {
                    if (name.equals("attributeNames")) {
                        return false;
                    }
                    return true;
                }
            };
            String json = JSON.toJSONString(wrapper, filter, SerializerFeature.WriteClassName);
            bytes = json.getBytes();
        } catch (Exception e) {
            logger.error("序列化session失败", e);
        }
        return bytes;
    }

    // 把byte还原为session
    protected MapSession byteToSession(byte[] bytes) {
        MapSession session = null;
        try {
            String json = new String(bytes);
            ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
            MapSessionWrapper wrapper = JSON.parseObject(json, MapSessionWrapper.class);
            ParserConfig.getGlobalInstance().setAutoTypeSupport(false);
            session = wrapper.getSession();
        } catch (Exception e) {
            logger.error("序列化session失败", e);
        }
        return session;
    }

    public RedisManager getRedisManager() {
        return redisManager;
    }

    public void setRedisManager(RedisManager redisManager) {
        this.redisManager = redisManager;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
