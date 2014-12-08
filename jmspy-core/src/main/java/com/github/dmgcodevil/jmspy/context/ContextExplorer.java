package com.github.dmgcodevil.jmspy.context;

/**
 * This interface provides ability to embed custom implementations in the lib in order to get
 * additional information about execution environment.
 * Can be useful when you need to know more than just method name and target class.
 *
 * @author dmgcodevil
 */
public interface ContextExplorer {

    /**
     * Gets info about root invocation context such as application name, request url and etc.
     * This method is invoked as soon as a invocation record is created i.e.
     * {@link com.github.dmgcodevil.jmspy.MethodInvocationRecorder#record(java.lang.reflect.Method, Object)} method is called.
     *
     * @return {@link InvocationContextInfo}
     */
    InvocationContextInfo getRootContextInfo();

    /**
     * Gets info about current invocation context such as page name and etc.
     * This method is invoked as soon as a method of proxy object is intercepted.
     *
     * @return {@link InvocationContextInfo}
     */
    InvocationContextInfo getCurrentContextInfo();
}
