package com.pai.base.session;

import org.springframework.session.MapSession;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * MapSession对象属性操作封装
 */
@SuppressWarnings("all")
public class MapSessionWrapper implements Serializable {

    private static final long serialVersionUID = 487767420000926827L;

    private MapSession session;

    private Map<String, Object> sessionAttrs;

    public MapSessionWrapper() {
    }

    public MapSessionWrapper(MapSession session) {
        this.session = session;
    }

    public MapSession getSession() {
        return session;
    }

    public void setSession(MapSession session) {
        this.session = session;
    }

    public Map<String, Object> getSessionAttrs() {
        try {
            Field field = session.getClass().getDeclaredField("sessionAttrs");
            field.setAccessible(true); //设置些属性是可以访问的
            Object val = field.get(session);//得到此属性的值
            if (val != null) {
                this.sessionAttrs = (Map<String, Object>) val;
            }
        } catch (Exception ex) {
        }
        return this.sessionAttrs;
    }

    public void setSessionAttrs(Map<String, Object> sessionAttrs) {
        this.sessionAttrs = sessionAttrs;
        try {
            Field field = session.getClass().getDeclaredField("sessionAttrs");
            field.setAccessible(true); //设置些属性是可以访问的
            field.set(this.session, this.sessionAttrs);
        } catch (Exception ex) {
        }
    }
}
