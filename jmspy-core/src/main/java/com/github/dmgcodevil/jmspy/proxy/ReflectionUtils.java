package com.github.dmgcodevil.jmspy.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Simple util class to perform operations related to java reflection framework.
 *
 * @author dmgcodevil
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
}
