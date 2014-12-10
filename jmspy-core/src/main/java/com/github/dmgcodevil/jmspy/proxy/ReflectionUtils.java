package com.github.dmgcodevil.jmspy.proxy;

import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * // todo
 *
 * @author  dmgcodevil
 */
public final class ReflectionUtils {

    private ReflectionUtils() {
    }

    public static List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        return getAllFields(fields, type);
    }

    public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != null) {
            fields = getAllFields(fields, type.getSuperclass());
        }

        return fields;
    }

    public static boolean hasDefaultConstructor(Class<?> type) {
        return getDefaultConstructor(type) != null;
    }

    public static Constructor getDefaultConstructor(Class<?> type) {
        try {
            return type.getDeclaredConstructor(new Class[]{});
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    @SuppressWarnings("restriction")
    @Deprecated
    public static <T> T newInstance(final Class<T> type) {

        try {
            if (hasDefaultConstructor(type)) {
                return type.newInstance();
            }
            final Constructor<?> constructor
                    = ReflectionFactory.getReflectionFactory()
                    .newConstructorForSerialization(
                            type, Object.class.getDeclaredConstructor());
            return type.cast(constructor.newInstance());
        } catch (NoSuchMethodException | InstantiationException e) {
            throw new UnsupportedOperationException(e);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

}
