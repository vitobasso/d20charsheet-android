package com.vituel.dndplayer.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Meant to allow customization of GUI components with hidden fields and methods in the Android framework
 *
 * Created by Victor on 10/01/14.
 */
public class ReflectionUtil {

    /**
     * Note: This only works for object parameters.
     * For primitive parameters call the one with a Class[] parameter.
     */
    public static <T> T createTemplateInstance(Object object, int templateIndex, Object... args) {
        ParameterizedType superClass = (ParameterizedType) object.getClass().getGenericSuperclass();
        Type type = superClass.getActualTypeArguments()[templateIndex];
        Class<T> instanceType;
        if (type instanceof ParameterizedType) {
            instanceType = (Class<T>) ((ParameterizedType) type).getRawType();
        } else {
            instanceType = (Class<T>) type;
        }
        return createInstance(instanceType, args);
    }

    public static <T> T createInstance(Class<T> instanceType, Object... args) {
        Class[] classes = getClasses(args);
        return createInstance(instanceType, classes, args);
    }

    private static <T> T createInstance(Class<T> instanceType, Class[] argClasses, Object[] args) {
        try {
            Constructor<T> cons = instanceType.getConstructor(argClasses);
            return cons.newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Note: This only works for object parameters.
     * For primitive parameters call the one with a Class[] parameter.
     */
    public static Object callPrivateMethod(Object obj, Class clazz, String methodName, Object... args) {
        Class[] argClasses = getClasses(args);
        return callPrivateMethod(obj, clazz, methodName, argClasses, args);
    }

    public static Object callPrivateMethod(Object obj, Class clazz, String methodName, Class[] argClasses, Object[] args) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, argClasses);
            method.setAccessible(true);
            Object ret = method.invoke(obj, args);
            return ret;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Note: Argument order is different to the varargs one because otherwise the compiler would prioritize it
     */
    public static Object callPrivateMethod(int arg1, boolean arg2, boolean arg3, int arg4, Object obj, Class clazz, String methodName) {
        Object[] args = {arg1, arg2, arg3, arg4};
        Class[] argClasses = {int.class, boolean.class, boolean.class, int.class};
        return callPrivateMethod(obj, clazz, methodName, argClasses, args);
    }

    public static Object getPrivateField(Object object, Class clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getPrivateField(Class clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setPrivateField(Object object, Class superclass, String fieldName, Object value) {
        try {
            Field field = superclass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static Class[] getClasses(Object[] args) {
        Class[] argClasses = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            argClasses[i] = args[i].getClass();
        }
        return argClasses;
    }

}
