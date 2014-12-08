package com.github.dmgcodevil.jmspy.context;

import com.github.dmgcodevil.jmspy.proxy.JMethod;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Represents information about invocation context.
 *
 * @author dmgcodevil
 */
public class InvocationContext implements Serializable {

    private static final long serialVersionUID = -766181999235753653L;

    private JMethod root;
    private StackTraceElement[] stackTrace;
    private InvocationContextInfo rootContextInfo;
    // used only to get InvocationContextInfo and shouldn't be serialized
    private transient ContextExplorer contextExplorer;

    public InvocationContext() {
    }

    public InvocationContext(Method root, StackTraceElement[] stackTrace, ContextExplorer contextExplorer) {
        if (root != null) {
            this.root = new JMethod(root);
        }
        this.stackTrace = stackTrace;
        this.contextExplorer = contextExplorer;
        if (contextExplorer != null) {
            this.rootContextInfo = contextExplorer.getRootContextInfo();
        }

    }

    public InvocationContext(Builder builder) {
        this(builder.root, builder.stackTrace, builder.contextExplorer);
    }

    public static Builder builder() {
        return new Builder();
    }

    public JMethod getRoot() {
        return root;
    }

    public StackTraceElement[] getStackTrace() {
        return stackTrace;
    }

    public InvocationContextInfo getRootContextInfo() {
        return rootContextInfo;
    }

    public ContextExplorer getContextExplorer() {
        return contextExplorer;
    }

    public static class Builder {
        private Method root;
        private StackTraceElement[] stackTrace;
        private ContextExplorer contextExplorer;

        /**
         * Sets root method that returns instrumented object.
         *
         * @param root the method
         * @return this Builder
         */
        public Builder root(Method root) {
            this.root = root;
            return this;
        }

        /**
         * Sets invocation stack trace.
         *
         * @param stackTrace the array of {@link StackTraceElement}
         * @return this Builder
         */
        public Builder stackTrace(StackTraceElement[] stackTrace) {
            this.stackTrace = stackTrace;
            return this;
        }

        /**
         * Sets specific {@link ContextExplorer} implementation in order to get additional info about execution environment.
         *
         * @param contextExplorer the context explorer
         * @return this Builder
         */
        public Builder contextExplorer(ContextExplorer contextExplorer) {
            this.contextExplorer = contextExplorer;
            return this;
        }

        public InvocationContext build() {
            return new InvocationContext(this);
        }
    }

}
