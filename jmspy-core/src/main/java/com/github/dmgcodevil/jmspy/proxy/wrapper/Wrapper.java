package com.github.dmgcodevil.jmspy.proxy.wrapper;

/**
 * Dedicated to wrap an objects if the proxy factory fails to create proxy.
 *
 * @author dmgcodevil
 */
public interface Wrapper<T> {

    /**
     * Sets target object into the wrapper.
     *
     * @param target the target object
     */
    void setTarget(T target);

    /**
     * Gets target object.
     *
     * @return the target object
     */
    T getTarget();

    /**
     * Gets current type (implementation class) of wrapper.
     *
     * @return the type of wrapper implementation
     */
    Class<? extends Wrapper<T>> getType();
}
