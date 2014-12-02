package com.github.dmgcodevil.jmspy.context;

import com.github.dmgcodevil.jmspy.proxy.JMethod;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public class InvocationContext implements Serializable {

    private static final long serialVersionUID = -766181999235753653L;
    private JMethod root;
    private StackTraceElement[] stackTrace;
    private InvocationContextInfo contextInfo;
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
            this.contextInfo = contextExplorer.getInfo();
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

    public ContextExplorer getContextExplorer() {
        return contextExplorer;
    }

    public InvocationContextInfo getContextInfo() {
        return contextInfo;
    }

    public static class Builder {
        private Method root;
        private StackTraceElement[] stackTrace;
        private ContextExplorer contextExplorer;

        public Builder root(Method root) {
            this.root = root;
            return this;
        }

        public Builder stackTrace(StackTraceElement[] stackTrace) {
            this.stackTrace = stackTrace;
            return this;
        }

        public Builder contextExplorer(ContextExplorer contextExplorer) {
            this.contextExplorer = contextExplorer;
            return this;
        }

        public InvocationContext build() {
            return new InvocationContext(this);
        }
    }

}
