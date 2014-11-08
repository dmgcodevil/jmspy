package com.github.dmgcodevil.jmspy.proxy.wrappers;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public interface Wrapper {

    Wrapper create(Object target);

    Class<?> getType();
}
