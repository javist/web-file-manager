package com.braintest.utils;

import java.lang.reflect.Field;

/**
 * Utils class for setter and getter class fields
 *
 * @author den, @date 29.09.2012 19:35:38
 */
public class ReflectionUtils {

    public static Object getField(Object o, String fieldName) {
        Field field = getAccessibleField(fieldName, o.getClass());
        Object fieldValue = null;
        try {
            fieldValue = field.get(o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return fieldValue;
    }

    public static void setField(Object o, String fieldName, Object inject) {
        Field field = getAccessibleField(fieldName, o.getClass());
        try {
            field.set(o, inject);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static Field getAccessibleField(String fieldName, Class<?> c) {
        Field field = null;
        while (c != null) {
            try {
                field = c.getDeclaredField(fieldName);
                field.setAccessible(true);
                break;
            } catch (NoSuchFieldException e) {
                c = c.getSuperclass();
            }
        }
        return field;
    }
}
