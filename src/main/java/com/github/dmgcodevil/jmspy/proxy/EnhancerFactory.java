package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import com.github.dmgcodevil.jmspy.graph.Node;
import com.github.dmgcodevil.jmspy.proxy.wrappers.JdkProxyWrapper;
import com.github.dmgcodevil.jmspy.proxy.wrappers.Wrapper;
import com.google.common.collect.Lists;
import com.google.common.primitives.Primitives;
import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.*;
import org.apache.commons.lang3.ClassUtils;
import org.objectweb.asm.Type;

import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.*;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.createIdentifier;
import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.getDefaultConstructor;

/**
 * Created by dmgcodevil on 11/7/2014.
 */
public class EnhancerFactory {

    public static final Type[] EMPTY_PARAMS = new Type[]{};
    public static final int MAIN_INTERCEPTOR = 0;
    public static final int SETTER_INTERCEPTOR = 1;
    public static final int GET_PROXY_ID_INTERCEPTOR = 2;

    public static Enhancer create(Object target, InvocationGraph invocationGraph) {


        //Create a dynamice interface

        InterfaceMaker im = new InterfaceMaker();
        Set<Signature> setters = Collections.emptySet();
        if (CommonUtils.isBean(target.getClass())) {
            setters = addSetters(im, target.getClass());

        }
        String id = createIdentifier();
        if (invocationGraph.getRoot() != null && invocationGraph.getRoot().getId() == null) {
            invocationGraph.getRoot().setId(id);
        }

        im.add(createGetProxyIdentifierMethod(), EMPTY_PARAMS);

        //Finish creating the interface
        Class proxyHelperInterface = im.create();

        Enhancer enhancer = new Enhancer();
        Class<?> superclass = target.getClass();

        enhancer.setSuperclass(superclass);

        List<Class<?>> interfaces = new ArrayList<>();

        if (target instanceof JdkProxyWrapper) {
            JdkProxyWrapper jdkProxyWrapper = new JdkProxyWrapper();
            interfaces.addAll(ClassUtils.getAllInterfaces(jdkProxyWrapper.getProxy().getClass()));

        } else {
            interfaces.addAll(ClassUtils.getAllInterfaces(target.getClass()));
        }

        interfaces.add(proxyHelperInterface);

        Callback[] callbacks = new Callback[]{
                new BasicMethodInterceptor(invocationGraph),
                new SetterInterceptor(invocationGraph),
                new ProxyIdentifierCallback(id)};

        enhancer.setInterfaces(interfaces.toArray(new Class<?>[interfaces.size()]));
        enhancer.setCallbackFilter(new BasicCallbackFilter(setters));
        enhancer.setCallbacks(callbacks);
        return enhancer;
    }

    private static Signature createGetProxyIdentifierMethod() {
        return new Signature(Constants.GET_PROXY_IDENTIFIER, Type.getType(String.class), EMPTY_PARAMS);
    }

    /**
     * If there are properties without setters then add setters in interface maker.
     *
     * @param im
     * @param type
     */
    private static Set<Signature> addSetters(InterfaceMaker im, Class<?> type) {
        Set<Signature> setters = new HashSet<>();
        List<PropertyDescriptor> propertyDescriptors = CommonUtils.getAllProperties(type);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if (propertyDescriptor.getWriteMethod() == null) {
                Type[] argumentTypes = new Type[]{
                        Type.getType(Primitives.wrap(propertyDescriptor.getPropertyType())) // looks like Type.getType doesn't work correctly with primitive types
                };
                String setterName = getSetterName(propertyDescriptor);
                Signature signature = new Signature(setterName, Type.VOID_TYPE, argumentTypes);
                im.add(signature, argumentTypes);
                setters.add(signature);
            } else {
                setters.add(createSignature(propertyDescriptor.getWriteMethod()));
            }
        }
        return setters;
    }

    private static Signature createSignature(Method method) {
        return new Signature(method.getName(), Type.getReturnType(method), Type.getArgumentTypes(method));
    }

    private static Set<Method> getGeneratedSetters(Class<?> type) {
        Set<Method> setters = new HashSet<>();
        for (Method method : type.getDeclaredMethods()) {
            if (method.getName().startsWith("set")) {
                setters.add(method);
            }
        }
        return setters;
    }

    private static String getSetterName(PropertyDescriptor propertyDescriptor) {
        return "set" + capitalize(propertyDescriptor.getName());
    }

    private static String capitalize(String base) {
        return base.substring(0, 1).toUpperCase() + base.substring(1);
    }

    private static class BasicCallbackFilter implements CallbackFilter {

        private Set<Signature> setters;

        private BasicCallbackFilter(Set<Signature> setters) {
            this.setters = setters;
        }

        @Override
        public int accept(Method method) {
            Signature signature = createSignature(method);
            System.out.println("apply filter: " + signature);
            if (method.getName().equals(Constants.GET_PROXY_IDENTIFIER)) {
                return GET_PROXY_ID_INTERCEPTOR;
            }
            if (setters.contains(signature)) {
                return SETTER_INTERCEPTOR;
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
