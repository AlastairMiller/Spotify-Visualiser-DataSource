package com.svd.mapper;

import com.google.common.testing.ArbitraryInstances;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RefinedObjectBuilder<T> {

    private final Class<?> clazz;
    private Map<String, Object> map;

    public RefinedObjectBuilder(Class<T> clazz) {
        this.clazz = clazz;
        this.map = new HashMap<>();
    }

    public static RefinedObjectBuilder<?> start(Class<?> clazz) {
        return new RefinedObjectBuilder<>(clazz);
    }

    public RefinedObjectBuilder<T> with(Map<String, Object> map) {
        this.map.putAll(map);
        return this;
    }

    private RefinedObjectBuilder<T> setProperty(T instance, String name, Object value) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, MalformedURLException {
        try {
            if (value != null) {
                invoke(instance, name, value, value.getClass());
            } else {
                findMethodAndInvoke(instance, name, null);
            }
        } catch (NoSuchMethodException e) {
            if (value.getClass() == String.class) {
                if (value.toString().contains("http")) {
                    invoke(instance, name, new URL(value.toString()), URL.class);
                } else if (name.equals("uri")) {
                    invoke(instance, name, URI.create(value.toString()), URI.class);
                }
            } else {
                findMethodAndInvoke(instance, name, value);
            }
        }
        return this;
    }

    private Object findMethodAndInvoke(T instance, String name, Object value) throws InvocationTargetException, IllegalAccessException {
        Method[] methods = clazz.getMethods();
        String setterName = getSetterName(name);
        for (Method method : methods) {
            if (method.getName().equals(setterName)) {
                return method.invoke(instance, value);

            }
        }

        throw new NoSuchMethodError("Cannot find method with name " + setterName);
    }

    private String getSetterName(String name) {
        return "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private void invoke(T instance, String name, Object value, Class<?> claz) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = clazz.getMethod(getSetterName(name), claz);
        method.invoke(instance, value);
    }

    public T build() {
        T instance = (T) ArbitraryInstances.get(clazz);
        try {
            for (Map.Entry<String, Object> val : map.entrySet()) {
                setProperty(instance, val.getKey(), val.getValue());
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to set value with builder", e);
        }
        return instance;
    }
}

