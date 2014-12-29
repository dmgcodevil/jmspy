package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.functional.Consumer;
import com.github.dmgcodevil.jmspy.proxy.wrapper.Wrapper;
import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.google.common.primitives.Primitives;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import javax.lang.model.type.NullType;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Util class for common needs.
 *
 * @author dmgcodevil
 */
public class CommonUtils {


    private static final Class<?> UNMODIFIABLE_COLLECTION;
    private static final Class<?> UNMODIFIABLE_MAP;
    private static final Set<Class<?>> EMPTY_DATA = new HashSet<>();

    static {
        try {
            UNMODIFIABLE_COLLECTION = Class.forName("java.util.Collections$UnmodifiableCollection");
            UNMODIFIABLE_MAP = Class.forName("java.util.Collections$UnmodifiableMap");
            EMPTY_DATA.add(Class.forName("java.util.Collections$EmptySet"));
            EMPTY_DATA.add(Class.forName("java.util.Collections$EmptyList"));
            EMPTY_DATA.add(Class.forName("java.util.Collections$EmptyMap"));
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
        return isNotPrimitiveOrWrapper(type) &&
                !isCollection(type) &&
                !isMap(type) &&
                !isArray(type) &&
                !type.isEnum();
    }

    public static Class<?> getOriginalType(Object obj) {
        if (obj == null) {
            return NullType.class;
        }
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

    public static boolean isEmptyData(Class<?> aClass) {
        return EMPTY_DATA.contains(aClass);
    }

    public static boolean isJdkProxy(Object target) {
        return target instanceof Proxy;
    }

    public static <K, V> void forEach(Map<K, V> map, Consumer<? super Map.Entry<K, V>> consumer) {
        if (MapUtils.isNotEmpty(map)) {
            for (Map.Entry<K, V> entry : map.entrySet()) {
                consumer.consume(entry);
            }
        }
    }

    public static <E> void forEach(Collection<E> collection, Consumer<? super E> consumer) {
        if (CollectionUtils.isNotEmpty(collection)) {
            for (E el : collection) {
                consumer.consume(el);
            }
        }
    }

    public static boolean isCustomWrapper(Class<?> type) {
        return !DefaultWrapper.class.isAssignableFrom(type) && Wrapper.class.isAssignableFrom(type);
    }

}
