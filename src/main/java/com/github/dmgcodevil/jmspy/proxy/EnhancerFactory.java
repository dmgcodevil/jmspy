package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.FixedValue;
import net.sf.cglib.proxy.InterfaceMaker;
import org.apache.commons.lang3.ClassUtils;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.createIdentifier;

/**
 * Created by dmgcodevil on 11/7/2014.
 */
public class EnhancerFactory {

    public static final Type[] EMPTY_PARAMS = new Type[]{};
    public static final int MAIN_INTERCEPTOR = 0;
    public static final int GET_PROXY_ID_INTERCEPTOR = 1;

    public static Enhancer create(Object target, InvocationGraph invocationGraph) {


        //Create a dynamice interface


        String id = createIdentifier();
        if (invocationGraph != null && invocationGraph.getRoot() != null && invocationGraph.getRoot().getId() == null) {
            invocationGraph.getRoot().setId(id);
        }
        InterfaceMaker im = new InterfaceMaker();
        im.add(createGetProxyIdentifierMethod(), EMPTY_PARAMS);

        //Finish creating the interface
        Class proxyHelperInterface = im.create();

        Enhancer enhancer = new Enhancer();
        Class<?> superclass = target.getClass();

        enhancer.setSuperclass(superclass);

        List<Class<?>> interfaces = new ArrayList<>();
        interfaces.addAll(ClassUtils.getAllInterfaces(target.getClass()));
        interfaces.add(proxyHelperInterface);

        Callback[] callbacks = new Callback[]{
                new BasicMethodInterceptor(invocationGraph),
                new ProxyIdentifierCallback(id)};

        enhancer.setInterfaces(interfaces.toArray(new Class<?>[interfaces.size()]));
        enhancer.setCallbackFilter(new BasicCallbackFilter());
        enhancer.setCallbacks(callbacks);
        return enhancer;
    }

    private static Signature createGetProxyIdentifierMethod() {
        return new Signature(Constants.GET_PROXY_IDENTIFIER, Type.getType(String.class), EMPTY_PARAMS);
    }


    private static Signature createSignature(Method method) {
        return new Signature(method.getName(), Type.getReturnType(method), Type.getArgumentTypes(method));
    }

    private static class BasicCallbackFilter implements CallbackFilter {
        @Override
        public int accept(Method method) {
            Signature signature = createSignature(method);
            //System.out.println("apply filter: " + signature);
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
}
