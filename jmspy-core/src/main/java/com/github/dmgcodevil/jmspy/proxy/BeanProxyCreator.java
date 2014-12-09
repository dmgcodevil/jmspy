package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.InvocationRecord;
import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import com.github.dmgcodevil.jmspy.proxy.wrappers.Wrapper;
import com.github.dmgcodevil.jmspy.reflection.Instantiator;

import java.util.Map;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.createIdentifier;

/**
 * Creates proxy for a java beans.
 */
public class BeanProxyCreator extends AbstractProxyCreator implements ProxyCreator {


    BeanProxyCreator(Map<Class<?>, Wrapper> wrappers) {
        super( wrappers);
    }

    @Override
    <T> T createProxy(T target, String proxyId, InvocationRecord invocationRecord) throws Throwable {

        Class<T> proxyClass = ProxyFactory.getInstance().createProxyClass(target, proxyId, invocationRecord);
        return Instantiator.getInstance().newInstance(proxyClass);

//        Object proxy = ProxyFactory.getInstance().create(target, invocationRecord, ProxyCreationStrategy.DELEGATE);
//        new BeanCopier(new SetProxyFieldInterceptor(ProxyFactory.getInstance(), invocationRecord)).copy(target, proxy);
//        return proxy;
    }

}
