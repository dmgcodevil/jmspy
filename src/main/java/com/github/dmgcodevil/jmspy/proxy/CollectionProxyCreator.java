package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import com.github.dmgcodevil.jmspy.proxy.wrappers.Wrapper;

import java.util.Collection;
import java.util.Map;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public class CollectionProxyCreator extends AbstractProxyCreator implements ProxyCreator {

    CollectionProxyCreator(InvocationGraph invocationGraph, Map<Class<?>, Wrapper> wrappers) {
        super(invocationGraph, wrappers);
    }

    @Override
    Object createProxy(Object target, Type type) throws Throwable {
        return createCollectionProxy(target, type);
    }

    private Object createCollectionProxy(Object target, Type type) throws Throwable {
        Collection col = (Collection) target;
        Class<?> componentType;
        if (col.isEmpty() || (componentType = getComponentType(type)) == null) {
            return EnhancerFactory.create(target, invocationGraph).create();
        } else {
            Collection proxy = (Collection) EnhancerFactory.create(target, invocationGraph).create();
            for (Object el : col) {
                proxy.add(ProxyCreatorFactory.create(componentType, invocationGraph, wrappers).create(el, new Type(el.getClass())));
            }
            return proxy;
        }
    }

    private static Class<?> getComponentType(Type type) {
        if (type.getParameterizedTypes() != null && type.getParameterizedTypes().length == 1) {
            return (Class<?>) type.getParameterizedTypes()[0];
        } else {
            return null;
        }
    }

}
