package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.exception.ProxyCreationException;
import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import com.github.dmgcodevil.jmspy.proxy.wrappers.Wrapper;
import com.google.common.base.Optional;
import com.google.common.base.Verify;

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
    public Object create(Object target) throws ProxyCreationException {
        return create(target, Type.builder().target(target.getClass()).build());
    }

    @Override
    public Object create(Object target, Type type) throws ProxyCreationException {
        if (target == null) {
            return null;
        }
        Optional<Wrapper> wrapperOptional = findWrapper(target.getClass());
        if (wrapperOptional.isPresent()) {
            Wrapper wrapper = wrapperOptional.get().create(target);
            Wrapper proxyWrapper = (Wrapper) EnhancerFactory.create(wrapper, invocationGraph).create();
            CommonUtils.copyProperties(proxyWrapper, wrapper);
            return proxyWrapper;
        } else {
            try {
                return createProxy(target, type);
            } catch (Throwable throwable) {
                throw new ProxyCreationException(throwable);
            }
        }
    }

    abstract Object createProxy(Object target, Type type) throws Throwable;

    /**
     * Tries find a wrapper for the given type.
     *
     * @param type the type to find certain wrapper
     * @return holder of result object. to check whether holder contains a (non-null) instance use {@link com.google.common.base.Optional#isPresent()}
     */
    Optional<Wrapper> findWrapper(Class<?> type) {
        for (Map.Entry<Class<?>, Wrapper> entry : wrappers.entrySet()) {
            if (entry.getKey().isAssignableFrom(type)) {
                return Optional.of(entry.getValue());
            }
        }
        return Optional.absent();
    }

}
