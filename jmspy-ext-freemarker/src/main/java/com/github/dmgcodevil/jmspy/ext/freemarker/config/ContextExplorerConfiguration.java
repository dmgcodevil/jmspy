package com.github.dmgcodevil.jmspy.ext.freemarker.config;

import com.google.common.annotations.Beta;

/**
 * Configuration for {@link com.github.dmgcodevil.jmspy.ext.freemarker.FreemarkerContextExplorer}.
 *
 * @author dmgcodevil
 */
public class ContextExplorerConfiguration {

    private static final ContextExplorerConfiguration DEFAULT = new ContextExplorerConfiguration();

    static {
        DEFAULT.setRecordRequestUrl(true);
        DEFAULT.setRecordDetails(true);
    }

    public static ContextExplorerConfiguration defaultConfig() {
        return DEFAULT;
    }

    private boolean recordDetails;

    private boolean recordRequestUrl;

    /**
     * Indicates that request url must be recorded.
     *
     * @return true - if request url must be recorded, otherwise - false
     */
    public boolean isRecordRequestUrl() {
        return recordRequestUrl;
    }

    /**
     * Specifies that an request url must be stored in {@link com.github.dmgcodevil.jmspy.context.InvocationContextInfo}.
     * Each http servlet request is converted to {@link com.github.dmgcodevil.jmspy.ext.freemarker.HttpServletRequestInfo}
     * and stored in {@link com.github.dmgcodevil.jmspy.ext.freemarker.HttpServletRequestInfoHolder}.
     * Ultimately each record in holder should be removed from holder, otherwise overflow in holder can cause OOM exception.
     * You can safety use this feature if you are sure that after each http request follow invocation of
     * {@link com.github.dmgcodevil.jmspy.MethodInvocationRecorder#record(java.lang.reflect.Method, Object)} method.
     * default value = true.
     *
     * @param recordRequestUrl true - if need to record request url, otherwise - false
     */
    @Beta
    public void setRecordRequestUrl(boolean recordRequestUrl) {
        this.recordRequestUrl = recordRequestUrl;
    }

    /**
     * Indicates that invocation details must be recorded.
     *
     * @return true - if need to record details, otherwise - false
     */
    public boolean isRecordDetails() {
        return recordDetails;
    }

    /**
     * Specifies that any additional info like FRL code and etc. must be recorded.
     * Using this feature can significantly increase memory usage.
     * default value = true.
     *
     * @param recordDetails true - if need to record details, otherwise - false
     */
    public void setRecordDetails(boolean recordDetails) {
        this.recordDetails = recordDetails;
    }
}
