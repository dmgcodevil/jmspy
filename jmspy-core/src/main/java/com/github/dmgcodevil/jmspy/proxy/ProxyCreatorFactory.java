package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.InvocationRecord;
import com.github.dmgcodevil.jmspy.exception.ProxyCreationException;
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

//        if (CommonUtils.isBean(type)) {
//            return new BeanProxyCreator(wrappers);
//        } else if (isArray(type)) {
//            return new ArrayProxyCreator(wrappers);
//        } else {
//            return primitiveProxyCreator;
//        }
    }

    @Deprecated
    private static ProxyCreator primitiveProxyCreator = new ProxyCreator() {

        @Override
        public <T> T create(T target, String proxyId, InvocationRecord invocationRecord) throws ProxyCreationException {
            return target;
        }
    };
}
