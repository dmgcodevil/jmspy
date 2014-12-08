package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.InvocationRecord;

import java.lang.reflect.Field;

/**
 * Created by dmgcodevil on 11/14/2014.
 */
public class SetProxyFieldInterceptor implements SetFieldInterceptor {

    public SetProxyFieldInterceptor(ProxyFactory proxyFactory, InvocationRecord invocationRecord) {
        this.proxyFactory = proxyFactory;
        this.invocationRecord = invocationRecord;
    }

    private final ProxyFactory proxyFactory;

    private InvocationRecord invocationRecord;

    @Override
    public void intercept(Object from, Field fromField, Object to, Field toField) throws Throwable {
        if (acceptable(fromField)) {
            Object source = fromField.get(from);
            Object proxy = proxyFactory.create(source, invocationRecord);
            toField.set(to, proxy);
        }
    }

    private boolean acceptable(Field field) {
        return field.getAnnotation(NotProxy.class) == null;
    }
}
