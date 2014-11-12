package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import com.github.dmgcodevil.jmspy.proxy.wrappers.Wrapper;

import java.lang.reflect.Array;
import java.util.Map;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isPrimitiveOrWrapper;

/**
 * Class description.
 *
 * @author Raman_Pliashkou
 */
public class ArrayProxyCreator extends AbstractProxyCreator implements ProxyCreator {


    ArrayProxyCreator(InvocationGraph invocationGraph, Map<Class<?>, Wrapper> wrappers) {
        super(invocationGraph, wrappers);
    }

    @Override
    Object createProxy(Object target, Type type) throws Throwable {
        if (target == null) {
            return null;
        }
        Object[] sourceArray = (Object[]) target;
        Class componentType = target.getClass().getComponentType();
        if (sourceArray.length == 0 || isPrimitiveOrWrapper(componentType)) {
            return sourceArray;
        }
        Object[] proxy = (Object[]) Array.newInstance(componentType, sourceArray.length);

        for (int i = 0; i < sourceArray.length; i++) {
            proxy[i] = ProxyCreatorFactory.create(componentType, invocationGraph, wrappers).create(sourceArray[i], Type.EMPTY); //create(invocationMessage, sourceArray[i]);
        }

        return proxy;
    }


}
