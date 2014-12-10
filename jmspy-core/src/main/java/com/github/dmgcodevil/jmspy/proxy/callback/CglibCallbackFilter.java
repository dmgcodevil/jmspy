package com.github.dmgcodevil.jmspy.proxy.callback;

import net.sf.cglib.proxy.CallbackFilter;

import java.lang.reflect.Method;

/**
 * // todo
 *
 * @author  dmgcodevil
 */
public class CglibCallbackFilter implements CallbackFilter {

    private static final CglibCallbackFilter INSTANCE = new CglibCallbackFilter();

    public static CglibCallbackFilter getInstance() {
        return INSTANCE;
    }

    @Override
    public int accept(Method method) {
        if (method.getName().equals(ProxyIdentifierCallback.GET_PROXY_IDENTIFIER)) {
            return ProxyIdentifierCallback.INDEX;
        }
        return BasicMethodInterceptor.INDEX;
    }
}