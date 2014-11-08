package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import com.github.dmgcodevil.jmspy.proxy.wrappers.Wrapper;

import java.util.Map;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isMap;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public class ProxyCreatorFactory {

    public static ProxyCreator create(Class<?> type, InvocationGraph invocationGraph, Map<Class<?>, Wrapper> wrappers) {
        if (CommonUtils.isBean(type)) {
            return new BeanProxyCreator(invocationGraph, wrappers);
        } else if (CommonUtils.isCollection(type)) {
            return new CollectionProxyCreator(invocationGraph, wrappers);
        } else if (isMap(type))
            return new MapProxyCreator(invocationGraph, wrappers);
        else {
            return primitiveProxyCreator;
            //throw new RuntimeException("failed to create proxy");
        }
    }

    private static ProxyCreator primitiveProxyCreator = new ProxyCreator() {
        @Override
        public Object create(Object target, Type type) {
            return target;
        }
    };
}
