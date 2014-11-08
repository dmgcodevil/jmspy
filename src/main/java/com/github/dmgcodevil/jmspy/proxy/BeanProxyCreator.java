package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import com.github.dmgcodevil.jmspy.proxy.wrappers.Wrapper;
import net.sf.cglib.beans.BeanCopier;

import java.util.Map;

/**
 * Creates proxy for a java bean.
 */
public class BeanProxyCreator extends AbstractProxyCreator implements ProxyCreator {


    BeanProxyCreator(InvocationGraph invocationGraph, Map<Class<?>, Wrapper> wrappers) {
        super(invocationGraph, wrappers);
    }

    @Override
    Object createProxy(Object target, Type type) throws Throwable {
        Object proxy = EnhancerFactory.create(target, invocationGraph).create();
        BeanCopier.create(target.getClass(), proxy.getClass(), false).copy(target, proxy, null);
        return proxy;
    }

}
