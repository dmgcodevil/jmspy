package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.InvocationRecord;
import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import com.github.dmgcodevil.jmspy.proxy.wrappers.Wrapper;

import java.util.Map;

/**
 * Creates proxy for a java beans.
 */
public class BeanProxyCreator extends AbstractProxyCreator implements ProxyCreator {


    BeanProxyCreator(InvocationRecord invocationRecord, Map<Class<?>, Wrapper> wrappers) {
        super(invocationRecord, wrappers);
    }

    @Override
    Object createProxy(Object target) throws Throwable {
        Object proxy = enhancerFactory.create(target, invocationRecord).create();
        new BeanCopier(new SetProxyFieldInterceptor(ProxyFactory.getInstance(), invocationRecord)).copy(target, proxy);
        return proxy;
    }

}
