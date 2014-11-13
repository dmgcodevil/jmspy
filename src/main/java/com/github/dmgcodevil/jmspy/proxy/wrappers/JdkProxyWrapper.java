package com.github.dmgcodevil.jmspy.proxy.wrappers;

import java.lang.reflect.Method;

/**
 * Class description.
 *
 * @author Raman_Pliashkou
 */
public class JdkProxyWrapper {

    private Object proxy;

    public Object getProxy() {
        return proxy;
    }

    public void setProxy(Object proxy) {
        this.proxy = proxy;
    }

    public Object invokeOnProxy(Method method, Object[] args) throws Throwable {
        return method.invoke(proxy, args);
    }
}
