package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import com.github.dmgcodevil.jmspy.proxy.wrappers.Wrapper;
import com.google.common.base.Predicate;
import com.google.common.base.Verify;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    private static volatile ProxyFactory instance;
    private static final Configuration DEF_CONFIGURATION = Configuration.builder().build();

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
    public Object create(Object target) {
        return create(target, InvocationGraph.create(target));
    }

    /**
     * Creates proxy for the given target object.
     *
     * @param target the object to create proxy
     * @param igraph the invocation graph
     * @return proxy
     */
    public Object create(Object target, InvocationGraph igraph) {
        if (target == null) {
            return null;
        }
        Verify.verifyNotNull(igraph, "invocation graph cannot be null");
        if (acceptable(target)) {
            return ProxyCreatorFactory.create(target.getClass(), igraph, wrappers)
                    .create(target);
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


}
