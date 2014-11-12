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
        MapType mapType = type.getMapComponentsTypes();


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

}
