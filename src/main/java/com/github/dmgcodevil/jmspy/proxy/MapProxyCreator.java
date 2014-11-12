package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import com.github.dmgcodevil.jmspy.proxy.wrappers.Wrapper;

import java.util.Map;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isNotPrimitiveOrWrapper;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public class MapProxyCreator extends AbstractProxyCreator implements ProxyCreator {


    MapProxyCreator(InvocationGraph invocationGraph, Map<Class<?>, Wrapper> wrappers) {
        super(invocationGraph, wrappers);
    }

    @Override
    Object createProxy(Object target, Type type) throws Throwable {
        return createMapProxy(target, type);
    }

    protected Object createMapProxy(Object target, Type type) throws Throwable {
        Map map = (Map) target;
        if (map.isEmpty()) {
            return EnhancerFactory.create(target, invocationGraph).create();
        }
        MapType mapType = type.getMapComponentsTypes();
        if (mapType.isEmpty()) {
            mapType = getComponentType(map);
        }

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

    // fallback
    private MapType getComponentType(Map map) {
        MapType.Builder builder = MapType.builder();
        Class<?> keyType = null;
        Class<?> valueType = null;
        for (Object entryObj : map.entrySet()) {
            Map.Entry entry = (Map.Entry) entryObj;
            Object key = entry.getKey();
            Object val = entry.getValue();
            if (key != null && keyType == null) {
                keyType = key.getClass();
            }
            if (val != null && valueType == null) {
                valueType = val.getClass();
            }
            if (keyType != null && valueType != null) {
                break;
            }
        }
        return builder.keyType(keyType).valueType(valueType).build();
    }


}
