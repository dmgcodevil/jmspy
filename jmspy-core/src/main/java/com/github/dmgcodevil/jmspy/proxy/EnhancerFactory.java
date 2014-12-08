package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.InvocationRecord;
import com.github.dmgcodevil.jmspy.functional.Producer;
import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import com.github.dmgcodevil.jmspy.proxy.callback.BasicMethodInterceptor;
import com.github.dmgcodevil.jmspy.proxy.callback.ProxyIdentifierCallback;
import com.google.common.base.Optional;
import com.google.common.base.Verify;
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



    private static final EnhancerFactory ENHANCER_FACTORY = new EnhancerFactory();

    public static EnhancerFactory getInstance() {
        return ENHANCER_FACTORY;
    }

    public synchronized Enhancer create(Object target) {
        return create(target, null);
    }

    public synchronized Enhancer create(Object target, final InvocationRecord invocationRecord) {
        Verify.verifyNotNull(target, "target object cannot be null");
        Class<?> targetClass = target.getClass();
        Enhancer enhancer = enhancerHolder.lookup(targetClass).orNull();
        if (enhancer == null) {
            enhancer = createNew(target, invocationRecord);
            enhancerHolder.hold(targetClass, enhancer);
            return enhancer;
        } else {
            String id = createIdentifier();
            initInvocationGraph(id, invocationRecord);
            Callback[] callbacks = new Callback[]{
                    new BasicMethodInterceptor(target, invocationRecord),
                    new ProxyIdentifierCallback(id)};
            enhancer.setCallbacks(callbacks);
        }
        return enhancer;
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

    private Enhancer createNew(Object target, InvocationRecord invocationRecord) {
        Class<?> type = target.getClass();
        String id = createIdentifier();
        initInvocationGraph(id, invocationRecord);
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
                new BasicMethodInterceptor(target, invocationRecord),
                new ProxyIdentifierCallback(id)};

        enhancer.setInterfaces(interfaces.toArray(new Class<?>[interfaces.size()]));
       // enhancer.setCallbackFilter(basicCallbackFilter);
        enhancer.setCallbacks(callbacks);


        return enhancer;
    }

    private static Signature createGetProxyIdentifierMethod() {
        return new Signature(ProxyIdentifierCallback.GET_PROXY_IDENTIFIER, Type.getType(String.class), EMPTY_PARAMS);
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
            data.put(new SoftReference<>(k), v);
        }
    }


}
