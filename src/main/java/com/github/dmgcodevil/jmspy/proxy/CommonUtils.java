package com.github.dmgcodevil.jmspy.proxy;

import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.google.common.primitives.Primitives;
import org.apache.commons.beanutils.BeanUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by dmgcodevil on 11/7/2014.
 */
public class CommonUtils {


    private static final Class<?> UNMODIFIABLE_COLLECTION;
    private static final Class<?> UNMODIFIABLE_MAP;

    static {
        try {
            UNMODIFIABLE_COLLECTION = Class.forName("java.util.Collections$UnmodifiableCollection");
            UNMODIFIABLE_MAP = Class.forName("java.util.Collections$UnmodifiableMap");
        } catch (ClassNotFoundException e) {
            throw Throwables.propagate(e);
        }
    }


    /**
     * Gets all properties for the specified type including all super classes except Object.class.
     *
     * @param type the to to get properties
     * @return the list of {@link PropertyDescriptor} objects
     */
    public static List<PropertyDescriptor> getAllProperties(Class type) {
        try {
            BeanInfo info = Introspector.getBeanInfo(type, Object.class);
            return Arrays.asList(info.getPropertyDescriptors());
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isPrimitiveOrWrapper(Class<?> type) {
        return type != null && (Primitives.allWrapperTypes().contains(type) || Primitives.allPrimitiveTypes().contains(type)
                || String.class.isAssignableFrom(type));
    }

    public static boolean isNotPrimitiveOrWrapper(Class<?> type) {
        return type != null && !isPrimitiveOrWrapper(type);
    }

    public static boolean isNotPrimitiveOrWrapper(Object o) {
        return o != null && !isPrimitiveOrWrapper(o.getClass());
    }

    public static boolean isArray(Class<?> type) {
        return type.isArray();
    }

    public static boolean isCollection(Class<?> type) {
        return Collection.class.isAssignableFrom(type);
    }

    public static boolean isMap(Class<?> type) {
        return Map.class.isAssignableFrom(type);
    }

    public static boolean isBean(Class<?> type) {
        return isNotPrimitiveOrWrapper(type) && !isCollection(type) && !isMap(type) && !isArray(type);
    }

    public static Class<?> getOriginalType(Object obj) {
        return isCglibProxy(obj) ? obj.getClass().getSuperclass() : obj.getClass();
    }

    public static boolean isCglibProxy(Object o) {
        return o.getClass().getName().contains("$$");
    }

    public static boolean isFinal(Class<?> type) {
        return Modifier.isFinal(type.getModifiers());
    }

    public static String createIdentifier() {
        return String.valueOf(IdGenerator.getInstance().generate());
    }

    public static void copyProperties(Object dest, Object orig) {
        try {
            BeanUtils.copyProperties(dest, orig);
        } catch (IllegalAccessException | InvocationTargetException e) {
            Throwables.propagate(e);
        }
    }

    public static Constructor getDefaultConstructor(Class<?> type) {
        try {
            return type.getDeclaredConstructor(new Class[]{});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object processUnmodifiable(Object source) throws Throwable {
        if (source == null) {
            return null;
        }
        Class<?> type = source.getClass();
        List<Field> fields = new ArrayList<>();
        fields = getAllFields(fields, type);
        if (UNMODIFIABLE_COLLECTION.isAssignableFrom(type)) {
            Field list = Iterables.tryFind(fields, new Predicate<Field>() {
                @Override
                public boolean apply(Field input) {
                    return "list".equals(input.getName()) || "c".equals(input.getName());
                }
            }).orNull();
            if (list == null) {
                throw new RuntimeException("failed to find fields 'list' / 'c' in: " + type.getCanonicalName());
            }
            list.setAccessible(true);
            return list.get(source);
        }
        if (UNMODIFIABLE_MAP.isAssignableFrom(type)) {
            Field map = Iterables.tryFind(fields, new Predicate<Field>() {
                @Override
                public boolean apply(Field input) {
                    return "m".equals(input.getName());
                }
            }).orNull();
            if (map == null) {
                throw new RuntimeException("failed to find field 'm' in: " + type.getCanonicalName());
            }
            map.setAccessible(true);
            return map.get(source);
        }
        return source;
    }

    public static boolean isUnmodifiable(Object source) {
        if (source == null) {
            return true;
        }
        Class<?> type = source.getClass();
        return UNMODIFIABLE_COLLECTION.isAssignableFrom(type) || UNMODIFIABLE_MAP.isAssignableFrom(type);
    }

    public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != null) {
            fields = getAllFields(fields, type.getSuperclass());
        }

        return fields;
    }


    public static boolean isJdkProxy(Object target) {
        return target instanceof Proxy;
    }

}
