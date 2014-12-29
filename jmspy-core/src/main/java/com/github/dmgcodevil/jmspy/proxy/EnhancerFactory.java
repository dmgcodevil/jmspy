package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.proxy.callback.BasicMethodInterceptor;
import com.github.dmgcodevil.jmspy.proxy.callback.CglibCallbackFilter;
import com.github.dmgcodevil.jmspy.proxy.callback.ProxyIdentifierCallback;
import com.github.dmgcodevil.jmspy.proxy.wrapper.Wrapper;
import com.google.common.collect.Lists;
import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InterfaceMaker;
import org.apache.commons.lang3.ClassUtils;
import org.objectweb.asm.Type;

import java.util.List;

/**
 * Factory to create {@link Enhancer}.
 * <p/>
 * @author dmgcodevil
 */
public class EnhancerFactory {

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

    public Enhancer createEnhancer(Class<?> type) {
        Enhancer enhancer = new Enhancer();
        //enhancer.setClassLoader(Thread.currentThread().getContextClassLoader()); // todo add ability to define class loader in Configuration
        enhancer.setSuperclass(type);
        enhancer.setCallbackTypes(new Class[]{ProxyIdentifierCallback.class, BasicMethodInterceptor.class});
        enhancer.setInterfaces(SERVICE_INTERFACES);
        enhancer.setCallbackFilter(CglibCallbackFilter.getInstance());
        return enhancer;
    }

    public Enhancer createEnhancerWithWrapper(Class<?> type, Class<? extends Wrapper> wrapper) {
        Enhancer enhancer = new Enhancer();
        //enhancer.setClassLoader(Thread.currentThread().getContextClassLoader()); // todo add ability to define class loader in Configuration
        enhancer.setSuperclass(wrapper);
        enhancer.setCallbackTypes(new Class[]{ProxyIdentifierCallback.class, BasicMethodInterceptor.class});
        List<Class> interfaces = Lists.newArrayList(SERVICE_INTERFACES);
        interfaces.addAll(ClassUtils.getAllInterfaces(type));
        enhancer.setInterfaces(interfaces.toArray(new Class[interfaces.size()]));
        enhancer.setCallbackFilter(CglibCallbackFilter.getInstance());
        return enhancer;
    }
}
