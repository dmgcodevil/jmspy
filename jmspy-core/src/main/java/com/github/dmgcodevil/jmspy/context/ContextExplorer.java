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
     * Gets info about execution environment such as application name, url and etc.
     *
     * @return {@link InvocationContextInfo}
     */
    InvocationContextInfo getInfo();
}
