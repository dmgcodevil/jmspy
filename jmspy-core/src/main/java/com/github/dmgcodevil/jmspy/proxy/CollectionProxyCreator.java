package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.InvocationRecord;
import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import com.github.dmgcodevil.jmspy.proxy.wrappers.Wrapper;

import java.util.Collection;
import java.util.Map;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isEmptyData;
import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.processUnmodifiable;

/**
 * {@link ProxyCreator} implementation that allows create proxy for {@link java.util.Collection} instances.
 *
 * Created by dmgcodevil.
 */
@Deprecated
public class CollectionProxyCreator extends AbstractProxyCreator implements ProxyCreator {

    CollectionProxyCreator(Map<Class<?>, Wrapper> wrappers) {
        super(wrappers);
    }

    @Override
   public  <T> T createProxy(T target, String proxyId, InvocationRecord invocationRecord) throws Throwable {
        return createCollectionProxy(target,  proxyId, invocationRecord);
    }

    private <T> T createCollectionProxy(T target,String proxyId,InvocationRecord invocationRecord) throws Throwable {
        if (!acceptable(target)) {
            return target;
        }
        target = (T)processUnmodifiable(target);

        return new BeanProxyCreator(wrappers).createProxy(target, proxyId, invocationRecord);

    }

    private boolean acceptable(Object target) {
        return target != null &&
                !isEmptyData(target.getClass());
    }

}
