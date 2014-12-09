package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.InvocationRecord;
import com.github.dmgcodevil.jmspy.exception.ProxyCreationException;
import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import com.github.dmgcodevil.jmspy.proxy.wrappers.Wrapper;

import java.util.Map;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isArray;
import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isMap;

/**
 * Factory to create instances of specific {@link ProxyCreator} implementations depending on type
 *
 * @author dmgcodevil
 */
public class ProxyCreatorFactory {

    public static ProxyCreator create(Class<?> type, Map<Class<?>, Wrapper> wrappers) {
        if (CommonUtils.isBean(type)) {
            return new BeanProxyCreator(wrappers);
        } else if (CommonUtils.isCollection(type)) {
            return new CollectionProxyCreator(wrappers);
        } else if (isMap(type)) {
            return new MapProxyCreator(wrappers);
        } else if (isArray(type)) {
            return new ArrayProxyCreator(wrappers);
        } else {
            return primitiveProxyCreator;
            //throw new RuntimeException("failed to create proxy");
        }
    }

    private static ProxyCreator primitiveProxyCreator = new ProxyCreator() {

        @Override
        public <T> T create(T target, String proxyId, InvocationRecord invocationRecord) throws ProxyCreationException {
            return target;
        }
    };
}
