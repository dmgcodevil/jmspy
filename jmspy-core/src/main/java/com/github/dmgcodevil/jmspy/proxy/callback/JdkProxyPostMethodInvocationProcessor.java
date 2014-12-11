package com.github.dmgcodevil.jmspy.proxy.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Specific {@link PostMethodInvocationProcessor}.
 * Don't use this directly in your project because it may be removed in the future.
 *
 * @author dmgcodevil
 */
public class JdkProxyPostMethodInvocationProcessor implements PostMethodInvocationProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdkProxyPostMethodInvocationProcessor.class);

    private static final PostMethodInvocationProcessor INSTANCE = new JdkProxyPostMethodInvocationProcessor();

    public static PostMethodInvocationProcessor getInstance() {
        return INSTANCE;
    }

    @Override
    public Object process(Object target) {
        try {
            if (target != null) {
                LOGGER.debug("{}.process: {}", getClass(), target.toString());
            }
        } catch (Throwable th) {
            // in the case if toString() throws exception
        }
        return target;
    }
}
