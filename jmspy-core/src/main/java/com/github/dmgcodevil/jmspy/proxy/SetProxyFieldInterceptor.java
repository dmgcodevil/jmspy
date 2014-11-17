package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.graph.InvocationGraph;

import java.lang.reflect.Field;

/**
 * Created by dmgcodevil on 11/14/2014.
 */
public class SetProxyFieldInterceptor implements SetFieldInterceptor {

    public SetProxyFieldInterceptor(ProxyFactory proxyFactory, InvocationGraph invocationGraph) {
        this.proxyFactory = proxyFactory;
        this.invocationGraph = invocationGraph;
    }

    private final ProxyFactory proxyFactory;

    private InvocationGraph invocationGraph;

    @Override
    public void intercept(Object from, Field fromField, Object to, Field toField) throws Throwable {
        if (acceptable(fromField)) {
            Object source = fromField.get(from);
            Object proxy = proxyFactory.create(source, invocationGraph);
            toField.set(to, proxy);
        }
    }

    private boolean acceptable(Field field) {
        return field.getAnnotation(NotProxy.class) == null;
    }
}
