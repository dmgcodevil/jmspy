package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.InvocationRecord;
import com.github.dmgcodevil.jmspy.exception.ProxyCreationException;
import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import com.github.dmgcodevil.jmspy.proxy.wrappers.Wrapper;
import com.google.common.base.Optional;

import java.util.Map;

/**
 * Basic implementation of {@link ProxyCreator}.
 * <p/>
 * Created by dmgcodevil.
 */
public abstract class AbstractProxyCreator implements ProxyCreator {

    protected InvocationRecord invocationRecord;
    protected Map<Class<?>, Wrapper> wrappers;
    protected EnhancerFactory enhancerFactory = EnhancerFactory.getInstance();

    protected AbstractProxyCreator(InvocationRecord invocationRecord, Map<Class<?>, Wrapper> wrappers) {
        this.invocationRecord = invocationRecord;
        this.wrappers = wrappers;
    }

    @Override
    public Object create(Object target) throws ProxyCreationException {
        if (target == null) {
            return null;
        }
        Optional<Wrapper> wrapperOptional = findWrapper(target.getClass());
        if (wrapperOptional.isPresent()) {
            Wrapper wrapper = wrapperOptional.get().create(target);
            Wrapper proxyWrapper = (Wrapper) enhancerFactory.create(wrapper, invocationRecord).create();
            proxyWrapper.setTarget(wrapper.getTarget());
            return proxyWrapper;
        } else {
            try {
                return createProxy(target);
            } catch (Throwable throwable) {
                throw new ProxyCreationException("type: " + target.getClass(), throwable);
            }
        }
    }

    /**
     * This abstract method is a part of template method {@link AbstractProxyCreator#createProxy(Object)}
     * and used to create proxies.
     *
     * @param target the target object
     * @return proxy
     * @throws Throwable in a case of any errors
     */
    abstract Object createProxy(Object target) throws Throwable;

    /**
     * Tries find a wrapper for the given type.
     *
     * @param type the type to find certain wrapper
     * @return holder of result object. to check whether holder contains a (non-null)
     * instance use {@link com.google.common.base.Optional#isPresent()}
     */
    Optional<Wrapper> findWrapper(Class<?> type) {
        Optional<Wrapper> optional = Optional.absent();
        for (Map.Entry<Class<?>, Wrapper> entry : wrappers.entrySet()) {
            if (entry.getKey().equals(type)) {
                optional = Optional.of(entry.getValue());
                break;
            }
        }

        // this iteration is required because if we deal with nested
        // or anonymous classes that are subclasses of some public classes or interfaces then condition based
        // on equals() between two classes isn't enough
        // for example unmodifiable set returns iterator that is anonymous class in unmodifiable
        // list and has next class name in runtime 'java.util.Collections$UnmodifiableCollection$1'
        // thus we need to use isAssignableFrom() to find wrapper

        if (!optional.isPresent()) {
            for (Map.Entry<Class<?>, Wrapper> entry : wrappers.entrySet()) {
                if (entry.getKey().isAssignableFrom(type)) {
                    optional = Optional.of(entry.getValue());
                    break;
                }
            }
        }
        return optional;
    }

}
