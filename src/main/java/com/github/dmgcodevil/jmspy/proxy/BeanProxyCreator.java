package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import com.github.dmgcodevil.jmspy.proxy.wrappers.Wrapper;

import java.util.Map;

/**
 * Creates proxy for a java bean.
 */
public class BeanProxyCreator extends AbstractProxyCreator implements ProxyCreator {


    BeanProxyCreator(InvocationGraph invocationGraph, Map<Class<?>, Wrapper> wrappers) {
        super(invocationGraph, wrappers);
    }

    @Override
    Object createProxy(Object target) throws Throwable {
        Object proxy = EnhancerFactory.create(target, invocationGraph).create();
        //BeanCopier.getInstance().copy(target, proxy);
        new BeanCopier(new SetProxyFieldInterceptor(ProxyFactory.getInstance(), invocationGraph)).copy(target, proxy);
        return proxy;
    }

}
