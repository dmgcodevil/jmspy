package com.github.dmgcodevil.jmspy.proxy.wrappers;

/**
 * Dedicated to wrap objects that don't have default constructor or cannot be instantiated for some reasons,
 * for instance iterator, map.entrySet(), Collections.unmodifiableSet() and etc.
 * Basically a wrapper implementation should delegate a calls to the target object thus client will
 * work with wrapper as with original object.
 *
 * @author dmgcodevil
 */
public interface Wrapper<T> {

    /**
     * Factory method. Creates a wrapper for the given target object.
     *
     * @param target the target object to be wrapped
     * @return new wrapper instance
     */
    Wrapper create(T target);

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
