package com.svd.mapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RefinedtoJsonMapper {

    public static Object invokeSimpleGetter(Method getter, Object inputObject) {
        try {
            Object nativeValue = getter.invoke(inputObject);
            if (getter.getReturnType().getName().equals("java.net.URL") | getter.getReturnType().getName().equals("java.net.URI")) {
                return nativeValue.toString();
            }
            return nativeValue;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
