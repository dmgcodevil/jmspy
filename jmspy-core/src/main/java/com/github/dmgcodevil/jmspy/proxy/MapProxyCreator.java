package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.InvocationRecord;
import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import com.github.dmgcodevil.jmspy.proxy.wrappers.Wrapper;

import java.util.Map;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isEmptyData;
import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.processUnmodifiable;

/**
 * {@link ProxyCreator} implementation that allows create proxy for {@link java.util.Map} instances.
 *
 * Created by dmgcodevil.
 */
public class MapProxyCreator extends AbstractProxyCreator implements ProxyCreator {


    MapProxyCreator( Map<Class<?>, Wrapper> wrappers) {
        super(wrappers);
    }

    @Override
    <T> T createProxy(T target, String proxyId, InvocationRecord invocationRecord) throws Throwable {
        return createMapProxy(target);
    }

    private <T>T createMapProxy(T target) throws Throwable {
        if (!acceptable(target)) {
            return target;
        }
        target = (T)processUnmodifiable(target);
        Map sourceMap = (Map) target;
        Map proxy = (Map) enhancerFactory.create(target, invocationRecord).create();
        if (sourceMap.isEmpty()) {
            return (T)proxy;
        }
        for (Object entryObj : sourceMap.entrySet()) {
            Map.Entry entry = (Map.Entry) entryObj;
            Object key = entry.getKey();
            Object val = entry.getValue();
            key = ProxyFactory.getInstance().create(key, invocationRecord);
            val = ProxyFactory.getInstance().create(val, invocationRecord);
            proxy.put(key, val);
        }
        return (T)proxy;
    }

    private boolean acceptable(Object target) {
        return target != null &&
                !isEmptyData(target.getClass());
    }

}
