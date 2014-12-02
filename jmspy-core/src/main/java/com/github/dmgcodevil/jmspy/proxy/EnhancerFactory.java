package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.functional.Producer;
import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import com.google.common.base.Optional;
import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.FixedValue;
import net.sf.cglib.proxy.InterfaceMaker;
import org.apache.commons.lang3.ClassUtils;
import org.objectweb.asm.Type;

import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.createIdentifier;

/**
 * Factory for {@link Enhancer}.
 * <p/>
 * Created by dmgcodevil.
 */
public class EnhancerFactory {

    public static final Type[] EMPTY_PARAMS = new Type[]{};
    public static final int MAIN_INTERCEPTOR = 0;
    public static final int GET_PROXY_ID_INTERCEPTOR = 1;


    private final Holder<Class<?>, Enhancer> enhancerHolder = new Holder<>();
    private final Holder<String, BasicMethodInterceptor> interceptorHolder = new Holder<>();

    private final BasicCallbackFilter basicCallbackFilter = new BasicCallbackFilter();

    private static final EnhancerFactory ENHANCER_FACTORY = new EnhancerFactory();

    public static EnhancerFactory getInstance() {
        return ENHANCER_FACTORY;
    }

    public synchronized Enhancer create(Object target, final InvocationGraph invocationGraph) {
        BasicMethodInterceptor basicMethodInterceptor = interceptorHolder.lookup(invocationGraph.getId(),
                new Producer<BasicMethodInterceptor>() {
                    @Override
                    public BasicMethodInterceptor produce() {
                        return new BasicMethodInterceptor(invocationGraph);
                    }
                });

        Class<?> targetClass = target.getClass();
        Optional<Enhancer> enhancerOpt = enhancerHolder.lookup(targetClass);
        Enhancer enhancer = enhancerOpt.orNull();
        if (enhancer == null) {
            enhancer = create(targetClass, invocationGraph);
            enhancerHolder.hold(targetClass, enhancer);
            return enhancer;
        } else {
            Callback[] callbacks = new Callback[]{
                    basicMethodInterceptor,
                    new ProxyIdentifierCallback(createIdentifier())};
            enhancer.setCallbacks(callbacks);
            return enhancer;
        }
    }

    private Enhancer create(Class<?> type, InvocationGraph invocationGraph) {
        String id = createIdentifier();
        if (invocationGraph != null && invocationGraph.getRoot() != null && invocationGraph.getRoot().getId() == null) {
            invocationGraph.getRoot().setId(id);
        }
        InterfaceMaker im = new InterfaceMaker();
        im.add(createGetProxyIdentifierMethod(), EMPTY_PARAMS);

        //Finish creating the interface
        Class proxyHelperInterface = im.create();

        Enhancer enhancer = new Enhancer();


        enhancer.setSuperclass(type);

        List<Class<?>> interfaces = new ArrayList<>();
        interfaces.addAll(ClassUtils.getAllInterfaces(type));
        interfaces.add(proxyHelperInterface);

        Callback[] callbacks = new Callback[]{
                new BasicMethodInterceptor(invocationGraph),
                new ProxyIdentifierCallback(id)};

        enhancer.setInterfaces(interfaces.toArray(new Class<?>[interfaces.size()]));
        enhancer.setCallbackFilter(basicCallbackFilter);
        enhancer.setCallbacks(callbacks);
        return enhancer;
    }

    private static Signature createGetProxyIdentifierMethod() {
        return new Signature(Constants.GET_PROXY_IDENTIFIER, Type.getType(String.class), EMPTY_PARAMS);
    }

    private static class BasicCallbackFilter implements CallbackFilter {
        @Override
        public int accept(Method method) {
            if (method.getName().equals(Constants.GET_PROXY_IDENTIFIER)) {
                return GET_PROXY_ID_INTERCEPTOR;
            }
            return MAIN_INTERCEPTOR;
        }
    }

    private static class ProxyIdentifierCallback implements FixedValue {
        private String id;

        private ProxyIdentifierCallback(String id) {
            this.id = id;
        }

        @Override
        public Object loadObject() throws Exception {
            return id;
        }
    }

    // not thread safe
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
            data.put(new SoftReference<K>(k), v);
        }
    }

}
