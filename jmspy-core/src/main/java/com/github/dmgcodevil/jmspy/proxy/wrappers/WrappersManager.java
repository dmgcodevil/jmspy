package com.github.dmgcodevil.jmspy.proxy.wrappers;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by dmgcodevil on 12/24/2014.
 */
public interface WrappersManager {

    void initWrappers() throws ClassNotFoundException;
    void registerWrapper(Class<?> aClass, Wrapper wrapper);
    void registerWrapper(String className, Wrapper wrapper) throws ClassNotFoundException;
    Map<Class<?>, Wrapper> getWrappers();

}
