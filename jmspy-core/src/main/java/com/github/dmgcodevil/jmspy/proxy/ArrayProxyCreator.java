package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.InvocationRecord;
import com.github.dmgcodevil.jmspy.proxy.wrapper.Wrapper;

import java.lang.reflect.Array;
import java.util.Map;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isPrimitiveOrWrapper;

/**
 * {@link ProxyCreator} implementation to create proxy for array.
 *
 * @author Raman_Pliashkou
 */
public class ArrayProxyCreator extends AbstractProxyCreator implements ProxyCreator {


    ArrayProxyCreator(Map<Class<?>, Class<? extends Wrapper>> wrappers) {
        super(wrappers);
    }

    @Override
    <T> T createProxy(T target, String proxyId, InvocationRecord invocationRecord) throws Throwable {
        if (target == null || !target.getClass().isArray()) {
            return target;
        }
        Object[] sourceArray = (Object[]) target;
        Class componentType = target.getClass().getComponentType();
        if (sourceArray.length == 0 || isPrimitiveOrWrapper(componentType)) {
            return (T)sourceArray;
        }
        Object[] proxy = (Object[]) Array.newInstance(componentType, sourceArray.length);

        for (int i = 0; i < sourceArray.length; i++) {
            ProxyFactory.getInstance().create(sourceArray[i]);
            proxy[i] = ProxyFactory.getInstance().create(sourceArray[i], invocationRecord);
        }

        return (T)proxy;
    }
}
