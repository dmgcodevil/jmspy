package com.github.dmgcodevil.jmspy.proxy.wrappers;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public interface Wrapper {

    /**
     * Factory method.
     *
     * @param target
     * @return
     */
    Wrapper create(Object target);

    void setTarget(Object target);

    Object getTarget();

    /**
     * Gets current type of wrapper.
     *
     * @return
     */
    Class<?> getType();
}
