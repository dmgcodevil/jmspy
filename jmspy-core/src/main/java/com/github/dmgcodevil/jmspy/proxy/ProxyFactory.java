package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.InvocationRecord;
import com.github.dmgcodevil.jmspy.functional.Producer;
import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import com.github.dmgcodevil.jmspy.proxy.callback.BasicMethodInterceptor;
import com.github.dmgcodevil.jmspy.proxy.callback.CglibCallbackFilter;
import com.github.dmgcodevil.jmspy.proxy.callback.ProxyIdentifierCallback;
import com.github.dmgcodevil.jmspy.proxy.wrappers.Wrapper;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Verify;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InterfaceMaker;
import org.objectweb.asm.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.createIdentifier;
import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isCglibProxy;
import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isEmptyData;
import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isJdkProxy;
import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isNotPrimitiveOrWrapper;

/**
 * Creates proxies for an objects.
 * <p/>
 * Created by dmgcodevil on 11/7/2014.
 */
public class ProxyFactory {

    private Map<Class<?>, Wrapper> wrappers = new HashMap<>();
    private Set<Class<?>> ignoreTypes = Sets.newHashSet();
    private Set<String> ignorePackages = Sets.newHashSet();
    private final Holder<Class<?>, Enhancer> enhancerHolder = new Holder<>();
    private static volatile ProxyFactory instance;
    private static final Configuration DEF_CONFIGURATION = Configuration.builder().build();
    public static final Type[] EMPTY_PARAMS = new Type[]{};

    private static final Signature GET_PROXY_IDENTIFIER_METHOD =
            new Signature(ProxyIdentifierCallback.GET_PROXY_IDENTIFIER, Type.getType(String.class), EMPTY_PARAMS);

    private static final Class PROXY_HELPER_INTERFACE;
    private static final Class[] SERVICE_INTERFACES;

    static {
        InterfaceMaker interfaceMaker = new InterfaceMaker();
        interfaceMaker.add(GET_PROXY_IDENTIFIER_METHOD, EMPTY_PARAMS);
        PROXY_HELPER_INTERFACE = interfaceMaker.create();
        SERVICE_INTERFACES = new Class[]{PROXY_HELPER_INTERFACE};
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyFactory.class);

    private ProxyFactory() {
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
                    instance = localInstance = new ProxyFactory();
                    instance.wrappers = config.getWrappers();
                    instance.ignoreTypes = config.getIgnoreTypes();
                    instance.ignorePackages = config.getIgnorePackages();
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
     * @return proxy
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


    protected boolean acceptable(Object target) {
        return target != null &&
                //Jdk proxies aren't supported because cannot be wrapped in CGLIB proxy without some preparatory work.
                !isJdkProxy(target) &&

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
            System.out.println("ignore: " + aClass);
            return true;
        }
        return false;
    }


    /**
     * @param target
     * @param proxyId
     * @param invocationRecord
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> Class<T> createProxyClass(Object target, String proxyId, InvocationRecord invocationRecord) {
        Verify.verifyNotNull(target, "target object cannot be null");
        Class<?> superClass = target.getClass();
//        if (target instanceof Wrapper) {
//            target = ((Wrapper) target).getTarget();
//        }

        Optional<Enhancer> enhancerOpt = enhancerHolder.lookup(superClass);

        if (enhancerOpt.isPresent()) {
            Enhancer enhancer = enhancerOpt.get();
            Class<T> proxyClass = enhancer.createClass();
            Enhancer.registerCallbacks(proxyClass, createCallbacks(target, proxyId, invocationRecord));
            return proxyClass;
        }
        Class<T> proxyClass;
        Enhancer enhancer = createEnhancer(superClass);
        try {
            proxyClass = enhancer.createClass();
        } catch (Throwable th) {
            LOGGER.error("failed to create proxy class for target type: '{}', error message: '{}'", superClass, th.getMessage());
            return null;
        }
        enhancerHolder.hold(superClass, enhancer);

        Enhancer.registerCallbacks(proxyClass, createCallbacks(target, proxyId, invocationRecord));

        return proxyClass;
    }

    private Callback[] createCallbacks(Object target, String proxyId, InvocationRecord invocationRecord) {
        return new Callback[]{
                new ProxyIdentifierCallback(proxyId),
                new BasicMethodInterceptor(target, invocationRecord),
        };
    }


    private Enhancer createEnhancer(Class<?> type) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(type);
        enhancer.setCallbackTypes(new Class[]{ProxyIdentifierCallback.class, BasicMethodInterceptor.class});
        enhancer.setInterfaces(SERVICE_INTERFACES);
        enhancer.setCallbackFilter(CglibCallbackFilter.getInstance());
        return enhancer;
    }

    private Enhancer lookupEnhancer(final Class<?> type) {
        return enhancerHolder.lookup(type, new Producer<Enhancer>() {
            @Override
            public Enhancer produce() {
                return createEnhancer(type);
            }
        });
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

}
