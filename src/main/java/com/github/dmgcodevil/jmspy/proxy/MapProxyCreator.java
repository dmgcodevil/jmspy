package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import com.github.dmgcodevil.jmspy.proxy.wrappers.Wrapper;

import java.util.Map;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isNotPrimitiveOrWrapper;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public class MapProxyCreator implements ProxyCreator {

    private InvocationGraph invocationGraph;
    private Map<Class<?>, Wrapper> wrappers;

    public MapProxyCreator(InvocationGraph invocationGraph, Map<Class<?>, Wrapper> wrappers) {
        this.invocationGraph = invocationGraph;
        this.wrappers = wrappers;
    }

    @Override
    public Object create(Object target, Type type) throws Throwable {
        return createMapProxy(target, type);
    }


    private Object createMapProxy(Object target, Type type) throws Throwable {
        Map map = (Map) target;
        if (map.isEmpty()) {
            return EnhancerFactory.create(target, invocationGraph).create();
        }
        MapType mapType = getComponentType(type);


        Map proxy = (Map) EnhancerFactory.create(target, invocationGraph).create();

        for (Object entryObj : map.entrySet()) {
            Map.Entry entry = (Map.Entry) entryObj;
            Object key = entry.getKey();
            Object val = entry.getValue();
            if (val != null && isNotPrimitiveOrWrapper(mapType.getKeyType())) {
                key = ProxyCreatorFactory.create(mapType.getKeyType(), invocationGraph, wrappers).create(key, new Type(key.getClass()));
            }
            if (val != null && isNotPrimitiveOrWrapper(mapType.getValueType())) {
                val = ProxyCreatorFactory.create(mapType.getValueType(), invocationGraph, wrappers).create(val, new Type(val.getClass()));
            }
            proxy.put(key, val);
        }
        return proxy;
    }

    private static MapType getComponentType(Type type) {
        if (type.getParameterizedTypes() != null && type.getParameterizedTypes().length == 2) {
            Class<?> keyType = (Class<?>) type.getParameterizedTypes()[0];
            Class<?> valueType = (Class<?>) type.getParameterizedTypes()[1];
            return new MapType(keyType, valueType);
        }
        return new MapType();
    }

    private static class MapType {
        Class<?> keyType;
        Class<?> valueType;

        private MapType() {
        }

        private MapType(Class<?> keyType, Class<?> valueType) {
            this.keyType = keyType;
            this.valueType = valueType;
        }

        public Class<?> getKeyType() {
            return keyType;
        }

        public String getKeyTypeName() {
            return keyType != null ? keyType.getCanonicalName() : "";
        }

        public void setKeyType(Class<?> keyType) {
            this.keyType = keyType;
        }

        public Class<?> getValueType() {
            return valueType;
        }

        public String getValueTypeName() {
            return valueType != null ? valueType.getCanonicalName() : "";
        }

        public void setValueType(Class<?> valueType) {
            this.valueType = valueType;
        }
    }
}
