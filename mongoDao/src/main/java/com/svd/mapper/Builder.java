package com.svd.mapper;

import com.google.common.testing.ArbitraryInstances;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Builder<T> {

    private final Class<?> clazz;
    private Map<String, Object> map;

    public Builder(Class<T> clazz) {
        super();
        this.clazz = clazz;
        this.map = new HashMap<>();
    }

    public static Builder<?> start(Class<?> clazz) {
        return new Builder<>(clazz);
    }

    public Builder<T> with(Map<String, Object> map) {
        this.map.putAll(map);
        return this;
    }

    private Builder<T> setProperty(T instance, String name, Object value) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, MalformedURLException {
        try {
            if (value != null) {
                invoke(instance, name, value, value.getClass());
            } else {
                findMethodAndInvoke(instance, name, null);
            }
        } catch (NoSuchMethodException e) {
            if (value.getClass() == java.lang.Integer.class) {
                invoke(instance, name, value, int.class);
            } else if (value.getClass() == java.lang.Boolean.class) {
                invoke(instance, name, value, boolean.class);
            } else if (value.getClass() == java.lang.String.class) {
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

    private void findMethodAndInvoke(T instance, String name, Object value) throws InvocationTargetException, IllegalAccessException {
        Method[] methods = clazz.getMethods();
        String setterName = getSetterName(name);
        boolean invoked = false;
        for (Method method : methods) {
            if (method.getName().equals(setterName)) {
                method.invoke(instance, value);
                invoked = true;
            }
        }
        if (!invoked) {
            throw new NoSuchMethodError("Cannot find method with name " + setterName);
        }
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

