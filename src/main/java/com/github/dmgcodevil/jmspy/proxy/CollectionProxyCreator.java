package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import com.github.dmgcodevil.jmspy.proxy.wrappers.Wrapper;

import java.util.Collection;
import java.util.Map;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isEmptyData;
import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.processUnmodifiable;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public class CollectionProxyCreator extends AbstractProxyCreator implements ProxyCreator {

    CollectionProxyCreator(InvocationGraph invocationGraph, Map<Class<?>, Wrapper> wrappers) {
        super(invocationGraph, wrappers);
    }

    @Override
    Object createProxy(Object target) throws Throwable {
        return createCollectionProxy(target);
    }

    private Object createCollectionProxy(Object target) throws Throwable {
        if (!acceptable(target)) {
            return target;
        }
        target = processUnmodifiable(target);
        Collection sourceCol = (Collection) target;
        Collection proxy = (Collection) EnhancerFactory.create(target, invocationGraph).create();
        if (sourceCol.isEmpty()) {
            return proxy;
        } else {
            for (Object el : sourceCol) {
                proxy.add(ProxyFactory.getInstance().create(el, invocationGraph));
            }
            return proxy;
        }
    }

    private boolean acceptable(Object target) {
        return target != null &&
                !isEmptyData(target.getClass());
    }

}
