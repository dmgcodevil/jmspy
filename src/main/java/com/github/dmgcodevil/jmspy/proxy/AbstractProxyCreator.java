package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import com.github.dmgcodevil.jmspy.proxy.wrappers.Wrapper;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Map;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public abstract class AbstractProxyCreator implements ProxyCreator {

    InvocationGraph invocationGraph;
    Map<Class<?>, Wrapper> wrappers;

    AbstractProxyCreator(InvocationGraph invocationGraph, Map<Class<?>, Wrapper> wrappers) {
        this.invocationGraph = invocationGraph;
        this.wrappers = wrappers;
    }

    @Override
    public Object create(Object target, Type type) throws Throwable {
        Wrapper wrapper = findWrapper(target.getClass());
        if (wrapper != null) {
            wrapper = wrapper.create(target);
            Wrapper proxyWrapper = (Wrapper) EnhancerFactory.create(wrapper, invocationGraph).create();
            CommonUtils.copyProperties(proxyWrapper, wrapper);
            //BeanUtils.copyProperties(proxyWrapper, wrapper);
            return proxyWrapper;
        } else {
            return createProxy(target, type);
        }

    }

    abstract Object createProxy(Object target, Type type) throws Throwable;

    Wrapper findWrapper(Class<?> type) {
        for (Map.Entry<Class<?>, Wrapper> entry : wrappers.entrySet()) {
            if (entry.getKey().isAssignableFrom(type)) {
                return entry.getValue();
            }
        }
        return null;
    }
}
