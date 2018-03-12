package com.svd.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RefinedtoJsonMapper {

    public static Class<?> simplifiedFieldDataType(Field field) {
        String fieldName = field.getType().toString();
        if (fieldName.contains("URL") | fieldName.contains("URI")) {
            return String.class;
        } else {
            return field.getType();
        }

    }

    public static Object invokeSimpleGetter(Method getter, Object inputObject){
        try {
            Object nativeValue = getter.invoke(inputObject);
            if(getter.getReturnType().getName().equals("java.net.URL") | getter.getReturnType().getName().equals("java.net.URI")){
                return nativeValue.toString();
            }
            return nativeValue;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
