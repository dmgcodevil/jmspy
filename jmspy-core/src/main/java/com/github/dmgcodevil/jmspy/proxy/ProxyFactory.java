package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.InvocationRecord;
import com.github.dmgcodevil.jmspy.functional.Producer;
import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import com.github.dmgcodevil.jmspy.proxy.callback.BasicMethodInterceptor;
import com.github.dmgcodevil.jmspy.proxy.callback.ProxyIdentifierCallback;
import com.github.dmgcodevil.jmspy.proxy.wrapper.Wrapper;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Verify;
import com.google.common.collect.Iterables;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.createIdentifier;
import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isCglibProxy;
import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isCustomWrapper;
import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isEmptyData;
import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isNotPrimitiveOrWrapper;

/**
 * Creates proxies for an objects.
 * <p/>
 *
 * @author dmgcodevil
 */
public class ProxyFactory {

    private final Map<Class<?>, Class<? extends Wrapper>> wrappers;
    private final Set<Class<?>> ignoreTypes;
    private final Set<String> ignorePackages;
    private final EnhancerFactory enhancerFactory;
    private final Holder<Class<?>, Enhancer> enhancerHolder = new Holder<>();
    private static volatile ProxyFactory instance;
    private static final Configuration DEF_CONFIGURATION = Configuration.builder().build();

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyFactory.class);

    private ProxyFactory(Configuration config) {
        wrappers = config.getWrappers();
        ignoreTypes = config.getIgnoreTypes();
        ignorePackages = config.getIgnorePackages();
        enhancerFactory = config.getEnhancerFactory();
    }

    /**
     * Creates new ProxyFactory instance if it has not been created yet.
     *
     * @param config the configuration
     * @return new or existing ProxyFactory instance
     */
    public static ProxyFactory getInstance(Configuration config) {
        ProxyFactory localInstance = instance;
        if (localInstance == null) {
            synchronized (ProxyFactory.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ProxyFactory(config);
                }
            }
        }
        return localInstance;
    }

    /**
     * Creates new ProxyFactory instance if it has not been created yet using default configuration.
     *
     * @return new or existing ProxyFactory instance
     */
    public static ProxyFactory getInstance() {
        return getInstance(DEF_CONFIGURATION);
    }

    /**
     * Creates proxy for the given target object and also created new invocation where the root node is target.
     *
     * @param target the object to create proxy
     * @return proxy
     */
    public <T> T create(T target) {
        return create(target, null);
    }

    /**
     * Creates proxy for the given target object.
     *
     * @param target           the object to create proxy
     * @param invocationRecord the invocation record
     * @return proxy for the given target object
     */
    public <T> T create(T target, InvocationRecord invocationRecord) {
        if (target == null) {
            return null;
        }
        if (acceptable(target)) {
            String proxyId = createIdentifier();
            initInvocationGraph(proxyId, invocationRecord);
            return ProxyCreatorFactory.create(target.getClass(), wrappers).create(target, proxyId, invocationRecord);

        }
        return target;
    }


    /**
     * Checks whether target object should be proxied or not.
     *
     * @param target the target object
     * @return true if target should be proxied, otherwise - false
     */
    protected boolean acceptable(Object target) {
        return target != null &&

                !isCglibProxy(target) &&

                // specific implementations such java.util.Collections$EmptySet, java.util.Collections$EmptyList are also not supported
                !isEmptyData(target.getClass()) &&

                // primitives and enums aren't supported
                isNotPrimitiveOrWrapper(target.getClass()) && !target.getClass().isEnum() &&

                // specific types that defined by user and must be ignored
                !ignore(target.getClass());
    }

    private boolean ignore(final Class<?> aClass) {
        boolean ignore = Iterables.tryFind(ignorePackages, new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return aClass.getName().startsWith(input);
            }
        }).isPresent();
        if (ignoreTypes.contains(aClass) || ignore) {
            LOGGER.debug("ignore type: '{}'", aClass);
            return true;
        }
        return false;
    }

    /**
     * Creates proxy class for the given target object.
     *
     * @param target           the target object
     * @param proxyId          the proxy id
     * @param invocationRecord the invocation record
     * @return proxy class or <code>null</code> if proxy class cannot be generated for the given target object
     */
    @SuppressWarnings("unchecked")
    public <T> Class<T> createProxyClass(Object target, String proxyId, InvocationRecord invocationRecord) {
        Verify.verifyNotNull(target, "target object cannot be null");
        Class<?> superClass = target.getClass();

        Optional<Enhancer> enhancerOpt = enhancerHolder.lookup(superClass);

        if (enhancerOpt.isPresent()) {
            Enhancer enhancer = enhancerOpt.get();
            Class<T> proxyClass = enhancer.createClass();
            Enhancer.registerCallbacks(proxyClass, createCallbacks(target, proxyId, invocationRecord));
            return proxyClass;
        }
        Class proxyClass;
        Enhancer enhancer = enhancerFactory.createEnhancer(superClass);
        CreateProxyClassOperation operation = createClass(enhancer);
        proxyClass = operation.proxyClass;
        if (proxyClass == null) {
            LOGGER.warn("failed to create proxy class for target type: '{}', error message: '{}'", superClass, operation.error);
            enhancer = enhancerFactory.createEnhancerWithWrapper(superClass, findWrapper(superClass));
            operation = createClass(enhancer);
            proxyClass = operation.proxyClass;
        }
        if (proxyClass == null) {
            LOGGER.error("failed to create proxy class for target type: '{}', error message: '{}'", superClass, operation.error);
            return null;
        }

        enhancerHolder.hold(superClass, enhancer);
        if (isCustomWrapper(proxyClass)) {
            target = null;
        }

        Enhancer.registerCallbacks(proxyClass, createCallbacks(target, proxyId, invocationRecord));

        return proxyClass;
    }

    private Callback[] createCallbacks(Object target, String proxyId, InvocationRecord invocationRecord) {
        return new Callback[]{
                new ProxyIdentifierCallback(proxyId),
                new BasicMethodInterceptor(target, invocationRecord),
        };
    }

    private CreateProxyClassOperation createClass(Enhancer enhancer) {
        CreateProxyClassOperation op = new CreateProxyClassOperation();
        try {
            op.proxyClass = enhancer.createClass();
        } catch (Throwable th) {
            op.error = th;
        }
        return op;
    }


    private void initInvocationGraph(String id, InvocationRecord invocationRecord) {
        if (invocationRecord != null) {
            initInvocationGraph(id, invocationRecord.getInvocationGraph());
        }
    }

    private void initInvocationGraph(String id, InvocationGraph invocationGraph) {
        if (invocationGraph != null && invocationGraph.getRoot() != null && invocationGraph.getRoot().getId() == null) {
            invocationGraph.getRoot().setId(id);
        }
    }

    /**
     * Tries find a wrapper for the given type.
     *
     * @param type the type to find certain wrapper
     * @return holder of result object. to check whether holder contains a (non-null)
     * instance use {@link com.google.common.base.Optional#isPresent()}
     */
    private Class<? extends Wrapper> findWrapper(Class<?> type) {
        Optional<Class<? extends Wrapper>> optional = Optional.absent();
        for (Map.Entry<Class<?>, Class<? extends Wrapper>> entry : wrappers.entrySet()) {
            if (entry.getKey().equals(type)) {
                optional = Optional.<Class<? extends Wrapper>>of(entry.getValue());
                break;
            }
        }

        // this iteration is required because if a wrapper was registered for an interface instead of class is used to create proxy
        // or we deal with nested or anonymous classes that are subclasses of some public classes or interfaces then condition based
        // on equals() to find wrapper isn't enough, thus we need to use isAssignableFrom() to find wrapper
        if (!optional.isPresent()) {
            for (Map.Entry<Class<?>, Class<? extends Wrapper>> entry : wrappers.entrySet()) {
                if (entry.getKey().isAssignableFrom(type)) {
                    optional = Optional.<Class<? extends Wrapper>>of(entry.getValue());
                    break;
                }
            }
        }
        return optional.or(Optional.of(DefaultWrapper.class)).get();
    }

    private static class Holder<K, V> {
        private final Map<SoftReference<K>, V> data = new WeakHashMap<>();

        private Optional<V> lookup(K key) {
            Optional<V> optional = Optional.absent();
            for (Map.Entry<SoftReference<K>, V> entry : data.entrySet()) {
                K currKey = entry.getKey().get();
                if (currKey != null && currKey.equals(key)) {
                    optional = Optional.of(entry.getValue());
                    break;
                }
            }
            return optional;
        }

        private V lookup(K key, Producer<V> valProducer) {
            Optional<V> optional = lookup(key);
            if (optional.isPresent()) {
                return optional.get();
            }
            V val = valProducer.produce();
            hold(key, val);
            return val;
        }

        private void hold(K k, V v) {
            data.put(new SoftReference<>(k), v);
        }
    }

    private static class CreateProxyClassOperation {
        Class proxyClass;
        Throwable error;
    }

}
