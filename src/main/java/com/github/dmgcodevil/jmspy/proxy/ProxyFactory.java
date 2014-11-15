package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import com.github.dmgcodevil.jmspy.proxy.wrappers.EntrySetWrapper;
import com.github.dmgcodevil.jmspy.proxy.wrappers.EntryWrapper;
import com.github.dmgcodevil.jmspy.proxy.wrappers.IteratorWrapper;
import com.github.dmgcodevil.jmspy.proxy.wrappers.MapKeySetWrapper;
import com.github.dmgcodevil.jmspy.proxy.wrappers.MapValuesWrapper;
import com.github.dmgcodevil.jmspy.proxy.wrappers.Wrapper;
import com.google.common.base.Throwables;
import com.google.common.base.Verify;
import com.google.common.collect.Sets;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
    private static volatile ProxyFactory instance;
    private static final Configuration DEF_CONFIGURATION = new Configuration();

    private ProxyFactory() {
    }

    public static ProxyFactory getInstance(Configuration config) {
        ProxyFactory localInstance = instance;
        if (localInstance == null) {
            synchronized (ProxyFactory.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ProxyFactory();
                    instance.wrappers = config.wrappers;
                    instance.ignoreTypes = config.ignoreTypes;
//                    if (config.setFieldInterceptor != null) {
//                        BeanCopier.getInstance().setSetFieldInterceptor(config.setFieldInterceptor);
//                    } else {
//                        BeanCopier.getInstance().setSetFieldInterceptor(new SetProxyFieldInterceptor(instance));
//                    }
                }
            }
        }
        return localInstance;
    }

    public static ProxyFactory getInstance() {
        return getInstance(DEF_CONFIGURATION);
    }

    public static Configuration configuration() {
        return new Configuration();
    }

    public Object create(Object target) {
        return create(target, InvocationGraph.create(target));
    }

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
                !isJdkProxy(target) &&
                !isEmptyData(target.getClass()) &&
                isNotPrimitiveOrWrapper(target.getClass()) &&
                !target.getClass().isEnum() &&
                !ignoreTypes.contains(target.getClass());
    }

    public static final class Configuration {
        private Map<Class<?>, Wrapper> wrappers = new HashMap<>();
        private SetFieldInterceptor setFieldInterceptor;
        private Set<Class<?>> ignoreTypes = Sets.newHashSet();

        public Configuration() {
            initWrappers();
        }

        private void initWrappers() {
            try {
                wrappers.put(Iterator.class, new IteratorWrapper());
                wrappers.put(Class.forName("java.util.HashMap$EntrySet"), new EntrySetWrapper());
                wrappers.put(Class.forName("java.util.HashMap$Values"), new MapValuesWrapper());
                wrappers.put(Class.forName("java.util.HashMap$KeySet"), new MapKeySetWrapper());
                wrappers.put(Map.Entry.class, new EntryWrapper());
            } catch (ClassNotFoundException e) {
                Throwables.propagate(e);
            }
        }

        public Configuration registerWrapper(Class<?> type, Wrapper wrapper) {
            wrappers.put(type, wrapper);
            return this;
        }

        public Configuration wrappers(Map<Class<?>, Wrapper> wrappers) {
            this.wrappers = wrappers;
            return this;
        }

        public Configuration setFieldInterceptor(SetFieldInterceptor setFieldInterceptor) {
            this.setFieldInterceptor = setFieldInterceptor;
            return this;
        }

        public Configuration ignoreTypes(Set<Class<?>> ignoreTypes) {
            this.ignoreTypes = ignoreTypes;
            return this;
        }

        public Configuration ignoreType(Class<?> type) {
            this.ignoreTypes.add(type);
            return this;
        }
    }

}
