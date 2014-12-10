package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.proxy.wrappers.Wrapper;

import java.util.Map;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isArray;

/**
 * Factory to create instances of specific {@link ProxyCreator} implementations depending on type
 *
 * @author dmgcodevil
 */
public class ProxyCreatorFactory {

    public static ProxyCreator create(Class<?> type, Map<Class<?>, Wrapper> wrappers) {

        if (isArray(type)) {
            return new ArrayProxyCreator(wrappers);
        }

        return new BeanProxyCreator(wrappers);
    }

}
