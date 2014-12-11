package com.github.dmgcodevil.jmspy.proxy.callback;

/**
 * Post processor that will be invoked after a method invocation on a target (no proxy object) object.
 *
 * @author dmgcodevil
 */
public interface PostMethodInvocationProcessor {

    Object process(Object target);
}
