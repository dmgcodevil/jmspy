package com.github.dmgcodevil.jmspy.proxy;

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

    private final Map<SoftReference<Class<?>>, Enhancer> enhancers = new WeakHashMap<>();
    private final BasicCallbackFilter basicCallbackFilter = new BasicCallbackFilter();

    private static final EnhancerFactory ENHANCER_FACTORY = new EnhancerFactory();

    public static EnhancerFactory getInstance() {
        return ENHANCER_FACTORY;
    }

    public synchronized Enhancer create(Object target, InvocationGraph invocationGraph) {
        Class<?> targetClass = target.getClass();
        Optional<Enhancer> enhancerOpt = lookupEnhancer(targetClass);
        Enhancer enhancer = enhancerOpt.orNull();
        if (enhancer == null) {
            enhancer = create(targetClass, invocationGraph);
            enhancers.put(new SoftReference<Class<?>>(targetClass), enhancer);
            return enhancer;
        } else {
            String id = createIdentifier();
            Callback[] callbacks = new Callback[]{
                    new BasicMethodInterceptor(invocationGraph),
                    new ProxyIdentifierCallback(id)};
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

    private Optional<Enhancer> lookupEnhancer(Class<?> type) {
        Optional<Enhancer> enhancerOptional = Optional.absent();
        for (Map.Entry<SoftReference<Class<?>>, Enhancer> entry : enhancers.entrySet()) {
            Class<?> key = entry.getKey().get();
            if (key != null && key.equals(type)) {
                enhancerOptional = Optional.of(entry.getValue());
                break;
            }
        }
        return enhancerOptional;
    }
}
