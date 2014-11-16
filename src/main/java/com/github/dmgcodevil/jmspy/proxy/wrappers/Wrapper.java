package com.github.dmgcodevil.jmspy.proxy.wrappers;

/**
 * Dedicated to wrap objects that don't have default constructor, for instance iterator, map.entrySet() and etc.
 * Basically a wrapper implementation should delegate a calls to the target object thus client will
 * work with wrapper as with original object.
 * <p/>
 * Created by dmgcodevil.
 */
public interface Wrapper {

    /**
     * Factory method. Creates a wrapper for the given target object.
     *
     * @param target the target object to be wrapped
     * @return new wrapper instance
     */
    Wrapper create(Object target);

    /**
     * Sets target object into the wrapper.
     *
     * @param target the target object
     */
    void setTarget(Object target);

    /**
     * Gets target object.
     *
     * @return the target object
     */
    Object getTarget();

    /**
     * Gets current type (implementation class) of wrapper.
     *
     * @return the type of wrapper implementation
     */
    Class<?> getType();
}
