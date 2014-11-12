package com.github.dmgcodevil.jmspy.proxy;

import com.google.common.base.Throwables;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.google.common.primitives.Primitives;
import org.apache.commons.beanutils.BeanUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by dmgcodevil on 11/7/2014.
 */
public class CommonUtils {


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
                || String.class.equals(type));
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
}
