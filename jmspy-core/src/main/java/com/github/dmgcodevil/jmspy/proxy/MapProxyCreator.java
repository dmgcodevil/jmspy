package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import com.github.dmgcodevil.jmspy.proxy.wrappers.Wrapper;

import java.util.Map;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isEmptyData;
import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.processUnmodifiable;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public class MapProxyCreator extends AbstractProxyCreator implements ProxyCreator {


    MapProxyCreator(InvocationGraph invocationGraph, Map<Class<?>, Wrapper> wrappers) {
        super(invocationGraph, wrappers);
    }

    @Override
    Object createProxy(Object target) throws Throwable {
        return createMapProxy(target);
    }

    private Object createMapProxy(Object target) throws Throwable {
        if (!acceptable(target)) {
            return target;
        }
        target = processUnmodifiable(target);
        Map sourceMap = (Map) target;
        Map proxy = (Map) EnhancerFactory.create(target, invocationGraph).create();
        if (sourceMap.isEmpty()) {
            return proxy;
        }
        for (Object entryObj : sourceMap.entrySet()) {
            Map.Entry entry = (Map.Entry) entryObj;
            Object key = entry.getKey();
            Object val = entry.getValue();
            key = ProxyFactory.getInstance().create(key, invocationGraph);
            val = ProxyFactory.getInstance().create(val, invocationGraph);
            proxy.put(key, val);
        }
        return proxy;
    }

    private boolean acceptable(Object target) {
        return target != null &&
                !isEmptyData(target.getClass());
    }

}
