package com.pai.base.utils;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectUtil {

    private static final Logger logger = LoggerFactory.getLogger(ObjectUtil.class);

    public static byte[] objectToBytes(Object obj) {
        byte[] result = null;
        ByteArrayOutputStream byteOutputStream = null;
        ObjectOutputStream objectOutputStream = null;

        try {
            byteOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteOutputStream);

            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();

            result = byteOutputStream.toByteArray();
        } catch (Exception e) {
            logger.error("objectToBytes error:",e);
        } finally {
            IOUtils.closeQuietly(objectOutputStream);
            IOUtils.closeQuietly(byteOutputStream);
        }

        return result;
    }

    public static Object bytesToObject(byte[] bytes) {

        Object result = null;
        ByteArrayInputStream byteInputStream = null;
        ObjectInputStream objectInputStream = null;

        try {
            byteInputStream = new ByteArrayInputStream(bytes);
            objectInputStream = new ObjectInputStream(byteInputStream);

            result = objectInputStream.readObject();

        } catch (Exception e) {
            logger.error("bytesToObject error:",e);
        } finally {
            IOUtils.closeQuietly(objectInputStream);
            IOUtils.closeQuietly(byteInputStream);
        }

        return result;
    }
}
