package com.github.dmgcodevil.jmspy.proxy;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public interface ProxyCreator {

    Object create(Object target,  Type type) throws Throwable;
}
