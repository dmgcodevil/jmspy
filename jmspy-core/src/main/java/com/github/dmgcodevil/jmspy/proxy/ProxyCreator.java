package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.exception.ProxyCreationException;

/**
 * Basic interface that defines several methods to create proxies.
 * <p/>
 * Created by dmgcodevil on 11/8/2014.
 */
public interface ProxyCreator {

    /**
     * Creates proxy of the target object.
     *
     * @param target the target object
     * @return the proxy for the target object
     * @throws ProxyCreationException in a case of any errors
     */
    Object create(Object target) throws ProxyCreationException;
}
