package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.InvocationRecord;
import com.github.dmgcodevil.jmspy.exception.ProxyCreationException;
import com.github.dmgcodevil.jmspy.proxy.wrapper.Wrapper;

import java.util.Map;

/**
 * Basic implementation of {@link ProxyCreator}.
 * <p/>
 *
 * @author dmgcodevil
 */
public abstract class AbstractProxyCreator implements ProxyCreator {

    protected Map<Class<?>, Class<? extends Wrapper>> wrappers;

    protected AbstractProxyCreator(Map<Class<?>, Class<? extends Wrapper>> wrappers) {
        this.wrappers = wrappers;
    }

    @Override
    public <T> T create(T target, String proxyId, InvocationRecord invocationRecord) throws ProxyCreationException {
        if (target == null) {
            return null;
        }
        try {
            return createProxy(target, proxyId, invocationRecord);
        } catch (Throwable throwable) {
            throw new ProxyCreationException("type: " + target.getClass(), throwable);
        }
    }

    /**
     * This abstract method is a part of template method
     * {@link AbstractProxyCreator#createProxy(Object, String, com.github.dmgcodevil.jmspy.InvocationRecord)}
     * and used to create proxies.
     *
     * @param target the target object
     * @return proxy
     * @throws Throwable in a case of any errors
     */
    abstract <T> T createProxy(T target, String proxyId, InvocationRecord invocationRecord) throws Throwable;

}
